package com.tm.worker.core.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.googlecode.aviator.AviatorEvaluator;
import com.tm.common.base.model.*;
import com.tm.common.dao.*;
import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.common.entities.autotest.enumerate.PlanExecuteResultStatusEnum;
import com.tm.common.entities.autotest.enumerate.PlanRunFromTypeEnum;
import com.tm.common.entities.autotest.enumerate.PlanRunTypeEnum;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.common.KeyValueRow;
import com.tm.worker.core.function.date.GetDate;
import com.tm.worker.core.function.random.GetRandomInt;
import com.tm.worker.core.function.random.GetRandomInt_1;
import com.tm.worker.core.function.random.GetRandomInt_2;
import com.tm.worker.core.protocol.jdbc.JDBCDataSourceFactory;
import com.tm.worker.core.threads.CaseResultLogProcessRunnerThread;
import com.tm.worker.core.threads.CaseTaskThread;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.service.CaseResultLogService;
import com.tm.worker.service.DbConfigService;
import com.tm.worker.utils.DataTypeAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Slf4j
@Component
public class TaskService {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().registerTypeAdapter(new TypeToken<Map<String, Object>>() {
            }.getType(),
            new DataTypeAdapter()).create();
    private static final Integer MAX_PLAN_CASE_TOTAL = 100;

    private ExecutorService executorService;
    private ThreadPoolExecutor threadPoolExecutor;

    private static final WorkerPlanTaskList<PlanTask> planTaskList = new WorkerPlanTaskList<>();
    private static final Map<Integer, WorkerCaseTaskQueue> caseTaskQueueMap = new HashMap<>();

    private static final Type groupVariablesTypeToken = new TypeToken<HashMap<String, String>[]>() {
    }.getType();

    public static final String GROUP_VARIABLE_RUN_FLAG = "__runFlag";
    public static final String GROUP_VARIABLE_GROUP_NAME = "__groupDescription";


    @Autowired
    private PlanCaseDao planCaseDao;
    @Autowired
    private PlanExecuteResultDao planExecuteResultDao;
    @Autowired
    private AutoCaseDao autoCaseDao;
    @Autowired
    private GlobalVariableDao globalVariableDao;
    @Autowired
    private DataNodeDao dataNodeDao;
    @Autowired
    private CaseResultLogService caseResultLogService;
    @Autowired
    private DbConfigService dbConfigService;
    @Autowired
    private ApiIpPortConfigDao apiIpPortConfigDao;
    @Autowired
    private JDBCDataSourceFactory jdbcDataSourceFactory;

    @PostConstruct
    public void init() {
        log.info("初始化worker线程池");
        initThreadPool();
        initAviatorEvaluator();
    }

    private void initAviatorEvaluator() {
        AviatorEvaluator.addFunction(new GetRandomInt());
        AviatorEvaluator.addFunction(new GetRandomInt_1());
        AviatorEvaluator.addFunction(new GetRandomInt_2());
        AviatorEvaluator.addFunction(new GetDate());
    }

    private void initThreadPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadFactory springThreadFactory = new CustomizableThreadFactory("worker-pool-");
        executorService = Executors.newFixedThreadPool(cores * 2, springThreadFactory);
        threadPoolExecutor = (ThreadPoolExecutor) executorService;
        CaseResultLogProcessRunnerThread caseResultLogProcessRunnerThread = new CaseResultLogProcessRunnerThread(caseResultLogService);
        threadPoolExecutor.submit(caseResultLogProcessRunnerThread);
    }

    @Async
    public void submitPlanTask(PlanExecuteResult planExecuteResult, PlanRunningConfigSnapshot snapshot) {
        log.info("接收到任务，计划结果id：{}，开始初始化", planExecuteResult.getId());
        if (!canSubmitPlanTask()) {
            log.info("任务队列已满，任务id：{}提交失败。", planExecuteResult.getId());
            planExecuteResultDao.setPlanExecuteResultEndStatus(planExecuteResult,
                    PlanExecuteResultStatusEnum.EXCEPTION, "任务队列已满");
            return;
        }
        AutoTestVariables planVariables = null;
        // 如果不是调试用例，需要加载全局变量
        if (!planExecuteResult.getFromType().equals(PlanRunFromTypeEnum.CASE.value())) {
            log.info("加载全局变量");
            planVariables = initPlanVariables(snapshot.getPlanVariables());
        }
        handleSubmitTask(planExecuteResult, snapshot, planVariables);
    }

    private AutoTestVariables initPlanVariables(String planVariables) {
        if(StringUtils.isBlank(planVariables)) {
            return null;
        }
        Type type = new TypeToken<ArrayList<KeyValueRow>>(){}.getType();
        List<KeyValueRow> rows = gson.fromJson(planVariables, type);
        Map<String, String> map = new HashMap<>();
        for (KeyValueRow row : rows) {
            map.put(row.getName(), row.getValue());
        }
        return new AutoTestVariables(map);
    }

    public void handleSubmitTask(PlanExecuteResult planExecuteResult,
                                 PlanRunningConfigSnapshot snapshot,
                                 AutoTestVariables planVariables) {
        List<Integer> caseIdList = new ArrayList<>();
        if (!fillCaseIdList(planExecuteResult, caseIdList)) return;

        if (caseIdList.isEmpty()) {
            log.info("计划没有关联用例， 计划id：{}，计划运行来源类型：{}", planExecuteResult.getPlanOrCaseId(),
                    planExecuteResult.getFromType());
            planExecuteResultDao.setPlanExecuteResultEndStatus(planExecuteResult,
                    PlanExecuteResultStatusEnum.EXCEPTION, "计划没有关联用例");
            return;
        }

        PlanTask planTask = new PlanTask(planExecuteResult, snapshot, planVariables);
        WorkerCaseTaskQueue caseTaskQueue = new WorkerCaseTaskQueue();
        int i = 0;
        for (Integer caseId : caseIdList) {
            AutoCase autoCase = autoCaseDao.selectByPrimaryId(caseId);
            // 判断是否有组合配置
            String groupVariablesStr = autoCase.getGroupVariables();
            // 组合配置不为空，并且是组合运行方式
            if (StringUtils.isNotBlank(groupVariablesStr) && PlanRunTypeEnum.GROUP.value().equals(snapshot.getRunType())) {
                HashMap<String, String>[] groups = gson.fromJson(groupVariablesStr, groupVariablesTypeToken);
                log.info("组合方式运行， 用例id: {}", caseId);
                for (int j = 0; j < groups.length; j++) {
                    HashMap<String, String> group = groups[j];
                    String taskId = planExecuteResult.getId() + "_" + i;
                    AutoTestVariables groupVariables = new AutoTestVariables();
                    if (group.containsKey(GROUP_VARIABLE_RUN_FLAG) && "1".equals(group.get(GROUP_VARIABLE_RUN_FLAG))) {
                        groupVariables = new AutoTestVariables(group);
                    }
                    CaseTask caseTask = new CaseTask(taskId, planTask, autoCase, groupVariables);
                    caseTask.setGroupNo(j);
                    if (group.containsKey(GROUP_VARIABLE_GROUP_NAME)) {
                        caseTask.setGroupName(group.get(GROUP_VARIABLE_GROUP_NAME));
                    }
                    caseTaskQueue.add(caseTask);
                    i++;
                }
            } else {
                log.info("非组合方式运行， 用例id: {}", caseId);
                String taskId = planExecuteResult.getId() + "_" + i;
                CaseTask caseTask = new CaseTask(taskId, planTask, autoCase);
                caseTaskQueue.add(caseTask);
            }
        }
        caseTaskQueueMap.put(planExecuteResult.getId(), caseTaskQueue);
        // 更新将运行的用例总数
        log.info("更新将运行的用例总数,结果id：{}, 总数: {}", planExecuteResult.getId(), caseTaskQueue.size());
        planExecuteResultDao.setTotal(planExecuteResult, caseTaskQueue.size());
        // 更新计划运行开始时间
        log.info("更新计划运行开始时间,结果id：{}", planExecuteResult.getId());
        planExecuteResultDao.setPlanExecuteResultStartTimestamp(planExecuteResult);
        planExecuteResultDao.setPlanExecuteResultStatus(planExecuteResult, PlanExecuteResultStatusEnum.INIT_END);

        planTask.setTotalCases(caseTaskQueue.size());
        planTaskList.add(planTask);
        planTaskList.sort();
    }

    private boolean fillCaseIdList(PlanExecuteResult planExecuteResult, List<Integer> caseIdList) {
        // 如果不是调试用例，查询计划用例关联列表
        if (!planExecuteResult.getFromType().equals(PlanRunFromTypeEnum.CASE.value())) {
            log.info("执行计划");
            CommonTableQueryBody body = new CommonTableQueryBody();
            body.setPageSize(MAX_PLAN_CASE_TOTAL);
            body.setOrder("seq");
            body.setSort("asc");
            body.setPlanId(planExecuteResult.getPlanOrCaseId());
            int total = planCaseDao.countList(body);
            if (total < 1) {
                log.info("计划没有关联用例， 计划id：{}，计划运行来源类型：{}", planExecuteResult.getPlanOrCaseId(),
                        planExecuteResult.getFromType());
                planExecuteResultDao.setPlanExecuteResultEndStatus(planExecuteResult,
                        PlanExecuteResultStatusEnum.EXCEPTION, "计划没有关联用例");
                return false;
            }
            //更新将运行的用例总数
            planExecuteResultDao.setTotal(planExecuteResult, total);

            List<PlanCase> planCases = planCaseDao.queryList(body);
            for (PlanCase planCase : planCases) {
                caseIdList.add(planCase.getCaseId());
            }
        } else {
            log.info("调试用例");
            caseIdList.add(planExecuteResult.getPlanOrCaseId());
        }

        return true;
    }

    public boolean canSubmitPlanTask() {
        return planTaskList.canSubmitPlanTask();
    }

    public boolean canSubmitCaseTask() {
        return threadPoolExecutor.getActiveCount() < threadPoolExecutor.getCorePoolSize();
    }

    public Future<BaseResponse> submitCaseTask(CaseTaskThread caseTask) {
        return threadPoolExecutor.submit(caseTask);
    }

    public boolean isPlanTaskEmpty() {
        return planTaskList.isEmpty();
    }

    public PlanTask getPlanTask(Integer index) {
        return planTaskList.get(index);
    }

    public WorkerCaseTaskQueue getCaseTaskQueue(Integer planResultId) {
        return caseTaskQueueMap.get(planResultId);
    }

    public void setPlanExecuteEnd(PlanTask planTask) {
        planExecuteResultDao.setPlanExecuteEnd(planTask.getPlanExecuteResult());
    }

    public void setPlanExecuteResultStatus(PlanTask planTask, PlanExecuteResultStatusEnum status) {
        planExecuteResultDao.setPlanExecuteResultStatus(planTask.getPlanExecuteResult(), status);
    }

    public void stopPlanTask(Integer planExecuteResultId) {
        caseTaskQueueMap.remove(planExecuteResultId);
        planTaskList.stop(planExecuteResultId);
    }

    public boolean removePlanTask(Integer planExecuteResultId) {
        caseTaskQueueMap.remove(planExecuteResultId);
        return planTaskList.remove(planExecuteResultId);
    }

    public void addFailCount(Integer planExecuteResultId) {
        planExecuteResultDao.addFailCount(planExecuteResultId);
    }

    public void addSuccessCount(Integer planExecuteResultId) {
        planExecuteResultDao.addSuccessCount(planExecuteResultId);
    }

    public void putResultLog(CaseExecuteLogOperate caseExecuteLogOperate) {
        caseResultLogService.put(caseExecuteLogOperate);
    }

    public Integer getSplitVariableTableType() {
        return caseResultLogService.getSplitVariableTableType();
    }

    public Integer getSplitCaseResultTableType() {
        return caseResultLogService.getSplitCaseResultTableType();
    }

    public Integer getSplitCaseStepResultTableType() {
        return caseResultLogService.getSplitCaseStepResultTableType();
    }

    public DbConfig findDbConfig(Integer envId, String dbName) {
        return dbConfigService.findDbConfig(envId, dbName);
    }

    public Connection getJDBCConnection(DbConfig dbConfig) {
        return jdbcDataSourceFactory.getConnection(dbConfig);
    }

    public void closeJDBCConnection(DbConfig dbConfig, Connection connection) {
        jdbcDataSourceFactory.closeConnection(dbConfig, connection);
    }

    public void setIsUpdateRunning(PlanTask planTask) {
        if(Boolean.FALSE.equals(planTask.getIsUpdateRunning())) {
            planTask.setIsUpdateRunning();
            setPlanExecuteResultStatus(planTask, PlanExecuteResultStatusEnum.RUNNING);
        }
    }

    public GlobalVariable selectGlobalVariableByPrimaryId(Integer id) {
        return globalVariableDao.selectByPrimaryId(id);
    }

    public List<DataNode> selectByDataTypeIdAndName(Integer dataTypeId, String name) {
        return dataNodeDao.selectByDataTypeIdAndName(dataTypeId, name);
    }

    public int updateGlobalVariable(GlobalVariable record) {
        return globalVariableDao.updateBySelective(record);
    }

    public List<ApiIpPortConfig> selectByUrlAndEnvId(String url, Integer envId) {
        return apiIpPortConfigDao.selectByUrlAndEnvId(url, envId);
    }
}
