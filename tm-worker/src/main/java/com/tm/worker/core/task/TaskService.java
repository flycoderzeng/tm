package com.tm.worker.core.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.googlecode.aviator.AviatorEvaluator;
import com.tm.common.base.model.*;
import com.tm.common.dao.AutoCaseDao;
import com.tm.common.dao.PlanCaseDao;
import com.tm.common.dao.PlanExecuteResultDao;
import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.common.entities.autotest.enumerate.PlanExecuteResultStatusEnum;
import com.tm.common.entities.autotest.enumerate.PlanRunFromTypeEnum;
import com.tm.common.entities.autotest.enumerate.PlanRunTypeEnum;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;


@Slf4j
@Component
public class TaskService {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
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
    private CaseResultLogService caseResultLogService;
    @Autowired
    private DbConfigService dbConfigService;
    @Autowired
    private JDBCDataSourceFactory jdbcDataSourceFactory;

    @PostConstruct
    public void init() {
        log.info("?????????worker?????????");
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
        log.info("??????????????????????????????id???{}??????????????????", planExecuteResult.getId());
        if (!canSubmitPlanTask()) {
            log.info("???????????????????????????id???{}???????????????", planExecuteResult.getId());
            planExecuteResultDao.setPlanExecuteResultEndStatus(planExecuteResult,
                    PlanExecuteResultStatusEnum.EXCEPTION, "??????????????????");
            return;
        }
        // ???????????????????????????????????????????????????
        if (!planExecuteResult.getFromType().equals(PlanRunFromTypeEnum.CASE.value())) {
            log.info("??????????????????");
        }
        handleSubmitTask(planExecuteResult, snapshot, null);
    }

    public void handleSubmitTask(PlanExecuteResult planExecuteResult,
                                 PlanRunningConfigSnapshot snapshot,
                                 AutoTestVariables globalVariables) {
        List<Integer> caseIdList = new ArrayList<>();
        if (!fillCaseIdList(planExecuteResult, caseIdList)) return;

        if (caseIdList.isEmpty()) {
            log.info("??????????????????????????? ??????id???{}??????????????????????????????{}", planExecuteResult.getPlanOrCaseId(),
                    planExecuteResult.getFromType());
            planExecuteResultDao.setPlanExecuteResultEndStatus(planExecuteResult,
                    PlanExecuteResultStatusEnum.EXCEPTION, "????????????????????????");
            return;
        }

        PlanTask planTask = new PlanTask(planExecuteResult, snapshot, globalVariables);
        WorkerCaseTaskQueue caseTaskQueue = new WorkerCaseTaskQueue();
        int i = 0;
        for (Integer caseId : caseIdList) {
            AutoCase autoCase = autoCaseDao.selectByPrimaryId(caseId);
            // ???????????????????????????
            String groupVariablesStr = autoCase.getGroupVariables();
            // ???????????????????????????????????????????????????
            if (StringUtils.isNotBlank(groupVariablesStr) && PlanRunTypeEnum.GROUP.value().equals(snapshot.getRunType())) {
                HashMap<String, String>[] groups = gson.fromJson(groupVariablesStr, groupVariablesTypeToken);
                log.info("????????????????????? ??????id: {}", caseId);
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
                log.info("???????????????????????? ??????id: {}", caseId);
                String taskId = planExecuteResult.getId() + "_" + i;
                CaseTask caseTask = new CaseTask(taskId, planTask, autoCase);
                caseTaskQueue.add(caseTask);
            }
        }
        caseTaskQueueMap.put(planExecuteResult.getId(), caseTaskQueue);
        // ??????????????????????????????
        log.info("??????????????????????????????,??????id???{}, ??????: {}", planExecuteResult.getId(), caseTaskQueue.size());
        planExecuteResultDao.setTotal(planExecuteResult, caseTaskQueue.size());
        // ??????????????????????????????
        log.info("??????????????????????????????,??????id???{}", planExecuteResult.getId());
        planExecuteResultDao.setPlanExecuteResultStartTimestamp(planExecuteResult);
        planExecuteResultDao.setPlanExecuteResultStatus(planExecuteResult, PlanExecuteResultStatusEnum.INIT_END);

        planTask.setTotalCases(caseTaskQueue.size());
        planTaskList.add(planTask);
        planTaskList.sort();
    }

    private boolean fillCaseIdList(PlanExecuteResult planExecuteResult, List<Integer> caseIdList) {
        // ?????????????????????????????????????????????????????????
        if (!planExecuteResult.getFromType().equals(PlanRunFromTypeEnum.CASE.value())) {
            log.info("????????????");
            CommonTableQueryBody body = new CommonTableQueryBody();
            body.setPageSize(MAX_PLAN_CASE_TOTAL);
            body.setOrder("seq");
            body.setSort("asc");
            body.setPlanId(planExecuteResult.getPlanOrCaseId());
            int total = planCaseDao.countList(body);
            if (total < 1) {
                log.info("??????????????????????????? ??????id???{}??????????????????????????????{}", planExecuteResult.getPlanOrCaseId(),
                        planExecuteResult.getFromType());
                planExecuteResultDao.setPlanExecuteResultEndStatus(planExecuteResult,
                        PlanExecuteResultStatusEnum.EXCEPTION, "????????????????????????");
                return false;
            }
            //??????????????????????????????
            planExecuteResultDao.setTotal(planExecuteResult, total);

            List<PlanCase> planCases = planCaseDao.queryList(body);
            for (PlanCase planCase : planCases) {
                caseIdList.add(planCase.getCaseId());
            }
        } else {
            log.info("????????????");
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
}
