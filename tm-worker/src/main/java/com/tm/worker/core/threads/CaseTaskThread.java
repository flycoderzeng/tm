package com.tm.worker.core.threads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tm.common.base.model.CaseExecuteResult;
import com.tm.common.base.model.CaseVariableValueResult;
import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.common.entities.autotest.enumerate.CaseExecuteResultStatusEnum;
import com.tm.common.entities.autotest.enumerate.LogOperateTypeEnum;
import com.tm.common.entities.autotest.enumerate.StepNodeTypeDefineEnum;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.LocalHostUtils;
import com.tm.common.utils.ResultUtils;
import com.tm.common.utils.TableSuffixUtils;
import com.tm.worker.core.control.GenericController;
import com.tm.worker.core.control.IfController;
import com.tm.worker.core.control.LoopController;
import com.tm.worker.core.control.WhileController;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.logic.Action;
import com.tm.worker.core.logic.SetUp;
import com.tm.worker.core.logic.TearDown;
import com.tm.worker.core.node.RootNode;
import com.tm.worker.core.node.StepNode;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.node.function.check.AssertNode;
import com.tm.worker.core.node.function.decoder.Base64DecodeNode;
import com.tm.worker.core.node.function.decoder.DecodeURIComponentNode;
import com.tm.worker.core.node.function.encoder.Base64EncodeNode;
import com.tm.worker.core.node.function.encoder.CipherCredentialEncodeNode;
import com.tm.worker.core.node.function.encoder.EncodeURIComponentNode;
import com.tm.worker.core.node.function.extractor.JsonMultiExtractorNode;
import com.tm.worker.core.node.function.extractor.XmlMultiExtractorNode;
import com.tm.worker.core.node.function.gvariables.GetGlobalKeyValueNode;
import com.tm.worker.core.node.function.gvariables.SetGlobalKeyValueNode;
import com.tm.worker.core.node.function.operation.OperationExpressionNode;
import com.tm.worker.core.node.function.randomizer.GetRandomIntNode;
import com.tm.worker.core.node.function.secure.Md5Node;
import com.tm.worker.core.node.function.string.SubStringNode;
import com.tm.worker.core.node.function.time.GetDateNode;
import com.tm.worker.core.node.function.time.GetTimestampNode;
import com.tm.worker.core.node.function.time.SleepNode;
import com.tm.worker.core.protocol.http.HttpSampler;
import com.tm.worker.core.protocol.jdbc.JDBCRequest;
import com.tm.worker.core.task.CaseTask;
import com.tm.worker.core.task.PlanTask;
import com.tm.worker.core.task.TaskService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CaseTaskThread implements Callable<BaseResponse> {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    // 单个步骤最大执行次数
    public static final int MAX_RUN_COUNT = 10000;

    private AutoTestContext autoTestContext;

    private StepNode caseStepTree;

    private PlanTask planTask;

    private CaseTask caseTask;

    private TaskService taskService;

    private volatile boolean running;

    private boolean error = false;
    private String resultInfo = null;

    private CaseExecuteResult caseExecuteResult;

    public CaseTaskThread(CaseTask caseTask,
                          TaskService taskService) {
        this.taskService = taskService;
        this.caseTask = caseTask;
        this.planTask = caseTask.getPlanTask();
    }

    @Override
    public BaseResponse call() {
        log.info("开始执行用例: {}", caseTask.getAutoCase().getId());
        running = true;
        StepNode currStepNode = null;
        StepNode teardownNode = null;
        try {
            log.info("计划正在执行的用例数加1");
            planTask.increaseRunningCount();
            taskService.setIsUpdateRunning(planTask);
            init();
            currStepNode = caseStepTree;
            while (running && currStepNode != null) {
                if (currStepNode.getType().equals(StepNodeTypeDefineEnum.TEARDOWN.value()) && teardownNode == null) {
                    teardownNode = currStepNode;
                    teardownNode.setEnded(true);
                    currStepNode = currStepNode.next();
                    teardownNode.setEnded(false);
                    continue;
                }
                currStepNode = runStepNode(currStepNode);
            }
        } catch (Exception exception) {
            log.error("run case error, ", exception);
            if (exception instanceof InterruptedException) {
                Thread.interrupted();
            }
            error = true;
            if(exception instanceof TMException) {
                resultInfo = exception.getMessage();
            }else{
                resultInfo = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(exception);
            }
            currStepNode.getDefine().logError(resultInfo);
            return ResultUtils.error(ResultCodeEnum.CASE_RUN_ERROR);
        } finally {
            try {
                currStepNode = teardownNode;
                if(currStepNode == null) {
                    currStepNode = findTeardownNode();
                }
                while (running && currStepNode != null) {
                    currStepNode = runStepNode(currStepNode);
                }
            } catch (Exception exception) {
                log.error("执行用例的teardown部分报错, ", exception);
                if (exception instanceof InterruptedException) {
                    Thread.interrupted();
                }
                currStepNode.getDefine().logError(exception.getMessage());
            }
            teardown();
        }
        return ResultUtils.success();
    }

    private StepNode findTeardownNode() {
        if (caseStepTree.getChildren() == null || caseStepTree.getChildren().isEmpty()) {
            return null;
        }
        for (int i = 0; i < caseStepTree.getChildren().size(); i++) {
            if(caseStepTree.getChildren().get(i).getType().equals(StepNodeTypeDefineEnum.TEARDOWN.value())) {
                return caseStepTree.getChildren().get(i);
            }
        }
        return null;
    }

    private StepNode runStepNode(StepNode currStepNode) throws Exception {
        currStepNode.getDefine().logStart(currStepNode.getKey());
        currStepNode.run();
        if ((currStepNode.getType().equals(StepNodeTypeDefineEnum.WHILE.value())
                || currStepNode.getType().equals(StepNodeTypeDefineEnum.LOOP.value()))
                && currStepNode.getRunCount() > MAX_RUN_COUNT) {
            log.info("用例id： {}，组合编号：{}，步骤：{}，执行超过最大次数{}限制", caseTask.getAutoCase().getId(),
                    caseTask.getGroupNo(), currStepNode.getDefine().getName(), MAX_RUN_COUNT);
            GenericController controller = (GenericController) currStepNode.getDefine();
            controller.setBreakLoop(true);
        }
        StepNode nextStepNode = currStepNode.next();
        currStepNode.getDefine().logSuccess();
        currStepNode = nextStepNode;
        return currStepNode;
    }

    private void teardown() {
        log.info("用例id： {}，组合编号：{} 执行结束", caseTask.getAutoCase().getId(), caseTask.getGroupNo());
        teardownCaseTask();
        AutoTestContextService.removeContext();
    }

    private void teardownCaseTask() {
        // 用例结果终态设置
        log.info("用例结果终态设置, 用例id：{}", caseTask.getAutoCase().getId());
        saveCaseResultStatus();
        // 用例变量值入库
        log.info("用例变量值入库, 用例id：{}", caseTask.getAutoCase().getId());
        saveAutoCaseVariableValueResult();
    }

    private void saveCaseResultStatus() {
        if(error) {
            caseExecuteResult.setResultStatus(CaseExecuteResultStatusEnum.FAIL.value());
            caseExecuteResult.setResultInfo(resultInfo);
        }else{
            caseExecuteResult.setResultStatus(CaseExecuteResultStatusEnum.SUCCESS.value());
        }
        caseExecuteResult.setEndTimestamp(System.currentTimeMillis());
        taskService.putResultLog(new CaseExecuteLogOperate(LogOperateTypeEnum.UPDATE, caseExecuteResult));
    }

    private void saveAutoCaseVariableValueResult() {
        AutoTestVariables caseVariables = autoTestContext.getCaseVariables();
        Map<String, Object> variables = caseVariables.getVariables();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            CaseVariableValueResult variableValueResult = new CaseVariableValueResult();
            variableValueResult.setPlanResultId(planTask.getPlanExecuteResultId());
            variableValueResult.setCaseId(caseTask.getAutoCase().getId());
            variableValueResult.setGroupNo(caseTask.getGroupNo());
            variableValueResult.setVariableName(entry.getKey());
            variableValueResult.setVariableValue(caseVariables.getVariableString(entry.getKey()));
            variableValueResult.setTableSuffix(TableSuffixUtils.getTableSuffix(new Date(planTask.getPlanExecuteResult().getStartTimestamp()),
                    taskService.getSplitVariableTableType(), 0));
            taskService.putResultLog(new CaseExecuteLogOperate(LogOperateTypeEnum.INSERT, variableValueResult));
        }
    }

    public void stop() {
        running = false;
    }

    public void change(Map node, StepNode parentNode, boolean first) {
        List<Map> children = null;
        if(node.containsKey("children")) {
            children = (List<Map>) node.remove("children");
        }
        Map define = null;
        if(node.containsKey("define")) {
            define = (Map) node.remove("define");
        }

        StepNode stepNode = BeanUtils.mapToBean(StepNode.class, node);
        if(first) {
            caseStepTree = stepNode;
        }

        stepNode.setDefine(getDefine(node, define));
        stepNode.setChildren(new ArrayList<>());
        stepNode.setParent(parentNode);
        if(parentNode != null) {
            parentNode.getChildren().add(stepNode);
        }
        if(children != null && !children.isEmpty()) {
            for (Map child : children) {
                change(child, stepNode, false);
            }
        }
    }

    public static StepNodeBase getDefine(Map node, Map defineMap) {
        String type = "root";
        if(node.containsKey("type")) {
            type = node.get("type").toString();
        }
        Pattern pattern = Pattern.compile("调用平台API\\((.*)\\)");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()) {
            type = matcher.group(1);
        }
        StepNodeBase define;
        StepNodeTypeDefineEnum nodeTypeDefineEnum = StepNodeTypeDefineEnum.get(type);
        if(nodeTypeDefineEnum == null) {
            log.info(type);
            throw new TMException("未定义的节点类型："  + type);
        }
        switch (nodeTypeDefineEnum) {
            case ROOT:
                define = BeanUtils.mapToBean(RootNode.class, defineMap);
                return define;
            case SETUP:
                define = BeanUtils.mapToBean(SetUp.class, defineMap);
                return define;
            case ACTION:
                define = BeanUtils.mapToBean(Action.class, defineMap);
                return define;
            case TEARDOWN:
                define = BeanUtils.mapToBean(TearDown.class, defineMap);
                return define;
            case HTTP:
            case HTTP_REQUEST:
                define = BeanUtils.mapToBean(HttpSampler.class, defineMap);
                return define;
            case JDBC:
            case JDBC_REQUEST:
                define = BeanUtils.mapToBean(JDBCRequest.class, defineMap);
                return define;
            case IF:
                define = BeanUtils.mapToBean(IfController.class, defineMap);
                return define;
            case LOOP:
                define = BeanUtils.mapToBean(LoopController.class, defineMap);
                return define;
            case WHILE:
                define = BeanUtils.mapToBean(WhileController.class, defineMap);
                return define;
            case __getRandomInt:
                define = BeanUtils.mapToBean(GetRandomIntNode.class, defineMap);
                return define;
            case __getDate:
                define = BeanUtils.mapToBean(GetDateNode.class, defineMap);
                return define;
            case __getTimestamp:
                define = BeanUtils.mapToBean(GetTimestampNode.class, defineMap);
                return define;
            case __sleep:
                define = BeanUtils.mapToBean(SleepNode.class, defineMap);
                return define;
            case __jsonMultiExtractor:
                define = BeanUtils.mapToBean(JsonMultiExtractorNode.class, defineMap);
                return define;
            case __xmlMultiExtractor:
                define = BeanUtils.mapToBean(XmlMultiExtractorNode.class, defineMap);
                return define;
            case __operationExpression:
                define = BeanUtils.mapToBean(OperationExpressionNode.class, defineMap);
                return define;
            case __encodeURIComponent:
                define = BeanUtils.mapToBean(EncodeURIComponentNode.class, defineMap);
                return define;
            case __decodeURIComponent:
                define = BeanUtils.mapToBean(DecodeURIComponentNode.class, defineMap);
                return define;
            case __base64Encode:
                define = BeanUtils.mapToBean(Base64EncodeNode.class, defineMap);
                return define;
            case __base64Decode:
                define = BeanUtils.mapToBean(Base64DecodeNode.class, defineMap);
                return define;
            case __md5:
                define = BeanUtils.mapToBean(Md5Node.class, defineMap);
                return define;
            case __subString:
                define = BeanUtils.mapToBean(SubStringNode.class, defineMap);
                return define;
            case __assert:
                define = BeanUtils.mapToBean(AssertNode.class, defineMap);
                return define;
            case __getGlobalKeyValue:
                define = BeanUtils.mapToBean(GetGlobalKeyValueNode.class, defineMap);
                return define;
            case __setGlobalKeyValue:
                define = BeanUtils.mapToBean(SetGlobalKeyValueNode.class, defineMap);
                return define;
            case __encodeCipherCredential:
                define = BeanUtils.mapToBean(CipherCredentialEncodeNode.class, defineMap);
                return define;
            default:
                log.info(type);
                throw new TMException("未定义处理类的节点类型："  + type);
        }
    }

    private void init() {
        AutoTestContext context = new AutoTestContext(caseTask, taskService);
        context.setCaseVariables(new AutoTestVariables());
        this.autoTestContext = context;
        AutoTestContextService.replaceContext(context);
        initCaseExecuteResult();
        initBuiltinVariables();
        initStepNodeTree();
    }

    private void initCaseExecuteResult() {
        log.info("初始化用例执行结果日志");
        CaseExecuteResult executeResult = new CaseExecuteResult();
        executeResult.setPlanResultId(planTask.getPlanExecuteResultId());
        executeResult.setCaseId(caseTask.getAutoCase().getId());
        executeResult.setGroupNo(caseTask.getGroupNo());
        executeResult.setGroupName(caseTask.getGroupName());
        executeResult.setStartTimestamp(System.currentTimeMillis());
        executeResult.setResultStatus(CaseExecuteResultStatusEnum.RUNNING.value());
        executeResult.setSeq(caseTask.getSeq());
        executeResult.setWorkerIp(LocalHostUtils.getLocalIp());
        executeResult.setName(caseTask.getAutoCase().getName());
        executeResult.setDescription(caseTask.getAutoCase().getDescription());
        executeResult.setSteps(caseTask.getAutoCase().getSteps());
        executeResult.setTableSuffix(TableSuffixUtils.getTableSuffix(new Date(planTask.getPlanExecuteResult().getStartTimestamp()),
                taskService.getSplitCaseResultTableType(), 0));
        taskService.putResultLog(new CaseExecuteLogOperate(LogOperateTypeEnum.INSERT, executeResult));
        this.caseExecuteResult = executeResult;
    }

    private void initStepNodeTree() {
        log.info("初始化用例步骤树");
        Map[] stepNodes = gson.fromJson(caseTask.getAutoCase().getSteps(), HashMap[].class);
        if(stepNodes == null) {
            throw new TMException("用例结构非法");
        }
        Map node = stepNodes[0];
        change(node, null, true);
    }

    private void initBuiltinVariables() {
        log.info("初始化用例内置变量值");
        AutoTestVariables caseVariables = autoTestContext.getCaseVariables();
        caseVariables.putObject(AutoTestVariables.BUILTIN_VARIABLE_NAME_CASE_ID, caseTask.getAutoCase().getId());
        caseVariables.put(AutoTestVariables.BUILTIN_VARIABLE_NAME_CASE_NAME, caseTask.getAutoCase().getName());
        caseVariables.putObject(AutoTestVariables.BUILTIN_VARIABLE_NAME_PLAN_ID, caseTask.getPlanTask().getPlanExecuteResult().getPlanOrCaseId());
        caseVariables.put(AutoTestVariables.BUILTIN_VARIABLE_NAME_PLAN_NAME, caseTask.getPlanTask().getPlanExecuteResult().getPlanOrCaseName());
        caseVariables.put(AutoTestVariables.BUILTIN_VARIABLE_NAME_WORKER_IP, caseTask.getPlanTask().getPlanExecuteResult().getWorkerIp());
        caseVariables.putObject(AutoTestVariables.BUILTIN_VARIABLE_NAME_PLAN_RESULT_ID, caseTask.getPlanTask().getPlanExecuteResult().getId());
        caseVariables.putObject(AutoTestVariables.BUILTIN_VARIABLE_NAME_ENV_ID, caseTask.getPlanTask().getRunningConfigSnapshot().getEnvId());
        caseVariables.put(AutoTestVariables.BUILTIN_VARIABLE_NAME_ENV_NAME, caseTask.getPlanTask().getRunningConfigSnapshot().getEnvName());
    }
}
