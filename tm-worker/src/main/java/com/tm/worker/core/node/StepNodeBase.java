package com.tm.worker.core.node;


import com.tm.common.base.model.CaseStepExecuteResult;
import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.common.entities.autotest.enumerate.CaseExecuteResultStatusEnum;
import com.tm.common.entities.autotest.enumerate.LogOperateTypeEnum;
import com.tm.common.utils.TableSuffixUtils;
import com.tm.worker.core.task.CaseTask;
import com.tm.worker.core.task.PlanTask;
import com.tm.worker.core.task.TaskService;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
@Data
public class StepNodeBase {
    protected String name;
    protected String comments;
    protected boolean enabled;
    private Integer seq;

    protected CaseStepExecuteResult caseStepExecuteResult;

    public void run() throws Exception {
    }

    protected StringBuilder resultInfoBuilder;

    public void init() {
        resultInfoBuilder = new StringBuilder();
        caseStepExecuteResult = new CaseStepExecuteResult();
    }

    public StepNodeBase addResultInfo(Object info) {
        resultInfoBuilder.append(info);
        return this;
    }

    public StepNodeBase addResultInfoLine(Object info) {
        resultInfoBuilder.append(info).append("\n");
        return this;
    }

    public String getResultInfo() {
        return resultInfoBuilder.toString();
    }

    public void logStart(String stepKey) {
        init();
        AutoTestContext context = AutoTestContextService.getContext();
        CaseTask caseTask = context.getCaseTask();
        PlanTask planTask = context.getPlanTask();
        TaskService taskService = context.getTaskService();
        log.info("插入步骤 开始执行 的日志,结果id：{}, 用例id: {}, 步骤名称: {}", planTask.getPlanExecuteResultId(),
                caseTask.getAutoCase().getId(), name);
        caseStepExecuteResult.setPlanResultId(planTask.getPlanExecuteResultId());
        caseStepExecuteResult.setCaseId(caseTask.getAutoCase().getId());
        caseStepExecuteResult.setGroupNo(caseTask.getGroupNo());
        caseStepExecuteResult.setDescription(comments);
        caseStepExecuteResult.setName(name);
        caseStepExecuteResult.setStartTimestamp(System.currentTimeMillis());
        caseStepExecuteResult.setStepKey(stepKey);
        caseStepExecuteResult.setResultStatus(CaseExecuteResultStatusEnum.RUNNING.value());
        caseStepExecuteResult.setTableSuffix(TableSuffixUtils.getTableSuffix(new Date(planTask.getPlanExecuteResult().getSubmitTimestamp()),
                taskService.getSplitCaseStepResultTableType(), 0));
        CaseExecuteLogOperate caseExecuteLogOperate = new CaseExecuteLogOperate(LogOperateTypeEnum.INSERT, caseStepExecuteResult);
        taskService.putResultLog(caseExecuteLogOperate);
    }

    public void logSuccess() {
        AutoTestContext context = AutoTestContextService.getContext();
        CaseTask caseTask = context.getCaseTask();
        PlanTask planTask = context.getPlanTask();
        TaskService taskService = context.getTaskService();
        log.info("插入步骤 成功执行 完毕的日志,结果id：{}, 用例id: {}, 步骤名称: {}", planTask.getPlanExecuteResultId(),
                caseTask.getAutoCase().getId(), name);
        caseStepExecuteResult.setResultInfo(getResultInfo());
        caseStepExecuteResult.setEndTimestamp(System.currentTimeMillis());
        caseStepExecuteResult.setResultStatus(CaseExecuteResultStatusEnum.SUCCESS.value());
        CaseExecuteLogOperate caseExecuteLogOperate = new CaseExecuteLogOperate(LogOperateTypeEnum.UPDATE, caseStepExecuteResult);
        taskService.putResultLog(caseExecuteLogOperate);
    }

    public void logError(String errorInfo) {
        AutoTestContext context = AutoTestContextService.getContext();
        CaseTask caseTask = context.getCaseTask();
        PlanTask planTask = context.getPlanTask();
        TaskService taskService = context.getTaskService();
        log.info("插入步骤 异常执行 完毕的日志,结果id：{}, 用例id: {}, 步骤名称: {}", planTask.getPlanExecuteResultId(),
                caseTask.getAutoCase().getId(), name);
        caseStepExecuteResult.setResultInfo(getResultInfo());
        caseStepExecuteResult.setEndTimestamp(System.currentTimeMillis());
        caseStepExecuteResult.setResultStatus(CaseExecuteResultStatusEnum.FAIL.value());
        CaseExecuteLogOperate caseExecuteLogOperate = new CaseExecuteLogOperate(LogOperateTypeEnum.UPDATE, caseStepExecuteResult);
        taskService.putResultLog(caseExecuteLogOperate);
    }
}
