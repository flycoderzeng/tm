package com.tm.worker.service;

import com.tm.common.base.mapper.*;
import com.tm.common.base.model.*;
import com.tm.common.entities.autotest.RunPlanBean;
import com.tm.common.entities.autotest.enumerate.PlanRunFromTypeEnum;
import com.tm.common.entities.autotest.request.GetPlanRunResultStatusBody;
import com.tm.common.entities.autotest.request.RunCaseBody;
import com.tm.common.entities.autotest.request.RunPlanBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.DateUtils;
import com.tm.common.utils.LocalHostUtils;
import com.tm.common.utils.ResultUtils;
import com.tm.worker.core.task.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("autoTestService")
public class AutoTestService {
    @Autowired
    private PlanExecuteResultMapper planExecuteResultMapper;
    @Autowired
    private DataNodeMapper dataNodeMapper;
    @Autowired
    private PlanRunningConfigSnapshotMapper planRunningConfigSnapshotMapper;
    @Autowired
    private RunEnvMapper runEnvMapper;
    @Autowired
    private AutoCaseMapper autoCaseMapper;
    @Autowired
    private AutoPlanMapper autoPlanMapper;
    @Autowired
    private TaskService taskService;

    public BaseResponse runAutoCase(RunCaseBody body, User loginUser) {
        BaseResponse checkRunCaseParams = checkRunCaseParams(body);
        if (checkRunCaseParams != null && checkRunCaseParams.getCode() != 0) return checkRunCaseParams;
        RunPlanBean bean = new RunPlanBean();
        bean.setCaseId(body.getCaseId());
        bean.setRunEnvId(body.getRunEnvId());
        bean.setRunType(body.getRunType());
        bean.setFromTypeEnum(PlanRunFromTypeEnum.CASE);
        bean.setRunDescription(body.getRunDescription());
        return runAutoPlan(bean, loginUser);
    }

    public BaseResponse runAutoPlan(RunPlanBody body, User loginUser) {
        BaseResponse checkRunPlanParams = checkRunPlanParams(body);
        if (checkRunPlanParams != null && checkRunPlanParams.getCode() != 0) return checkRunPlanParams;
        RunPlanBean bean = new RunPlanBean();
        bean.setPlanId(body.getPlanId());
        bean.setRunType(body.getRunType());
        bean.setRunEnvId(body.getRunEnvId());
        bean.setFromTypeEnum(PlanRunFromTypeEnum.get(body.getFromType()));
        bean.setPlanCronJobId(body.getPlanCronJobId());
        bean.setPriority(body.getPriority());
        bean.setPlanVariables(body.getPlanVariables());
        bean.setRunDescription(body.getRunDescription());
        return runAutoPlan(bean, loginUser);
    }

    private BaseResponse checkRunPlanParams(RunPlanBody body) {
        if(body.getRunEnvId() != null) {
            RunEnv byId = runEnvMapper.findById(body.getRunEnvId());
            if(byId == null) {
                return ResultUtils.error(ResultCodeEnum.RUN_ENV_NOT_EXISTS);
            }
        }
        AutoPlan autoPlan = autoPlanMapper.selectByPrimaryId(body.getPlanId());
        if(autoPlan == null) {
            return ResultUtils.error(ResultCodeEnum.AUTO_PLAN_NOT_EXISTS);
        }

        return null;
    }

    private BaseResponse checkRunCaseParams(RunCaseBody body) {
        if(body.getRunEnvId() != null) {
            RunEnv byId = runEnvMapper.findById(body.getRunEnvId());
            if(byId == null) {
                return ResultUtils.error(ResultCodeEnum.RUN_ENV_NOT_EXISTS);
            }
        }
        AutoCase autoCase = autoCaseMapper.selectByPrimaryId(body.getCaseId());
        if(autoCase == null) {
            return ResultUtils.error(ResultCodeEnum.AUTO_CASE_NOT_EXISTS);
        }

        return null;
    }

    public BaseResponse runAutoPlan(RunPlanBean bean, User loginUser) {
        log.info("{} run plan {}", loginUser.getUsername(), bean.getPlanId());
        if(!taskService.canSubmitPlanTask()) {
            return ResultUtils.error(ResultCodeEnum.COPY_TASK_OVERFLOW_ERROR);
        }
        PlanExecuteResult planExecuteResult = newPlanExecuteResult(bean, loginUser);
        if(planExecuteResult == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        log.info("新增 planExecuteResult 成功, {}", planExecuteResult.getId());
        PlanRunningConfigSnapshot snapshot = newPlanRunningConfigSnapshot(planExecuteResult, bean);
        log.info("新增 计划运行时配置成功, {}", snapshot.getPlanResultId());
        if(bean.getFromTypeEnum() == PlanRunFromTypeEnum.CASE) {
            updateAutoCaseLastRunEnvId(bean);
        }
        submitPlanTask(planExecuteResult, snapshot);

        return ResultUtils.success(planExecuteResult.getId());
    }

    public void submitPlanTask(PlanExecuteResult planExecuteResult, PlanRunningConfigSnapshot snapshot) {
        taskService.submitPlanTask(planExecuteResult, snapshot);
    }

    private void updateAutoCaseLastRunEnvId(RunPlanBean body) {
        AutoCase autoCase = autoCaseMapper.selectByPrimaryId(body.getCaseId());
        autoCase.setLastRunEnvId(body.getRunEnvId());
        autoCaseMapper.updateBySelective(autoCase);
    }

    private PlanRunningConfigSnapshot newPlanRunningConfigSnapshot(PlanExecuteResult planExecuteResult, RunPlanBean runPlanBean) {
        AutoPlan autoPlan =  null;
        if(runPlanBean.getFromTypeEnum() == PlanRunFromTypeEnum.PLAN || runPlanBean.getFromTypeEnum() == PlanRunFromTypeEnum.CRON_JOB) {
            autoPlan = autoPlanMapper.selectByPrimaryId(runPlanBean.getPlanId());
        }
        PlanRunningConfigSnapshot snapshot = new PlanRunningConfigSnapshot();
        snapshot.setPlanResultId(planExecuteResult.getId());
        if(runPlanBean.getRunEnvId() == null) {
            snapshot.setEnvId(autoPlan.getEnvId());
        }else{
            snapshot.setEnvId(runPlanBean.getRunEnvId());
        }
        if(runPlanBean.getFromTypeEnum() == PlanRunFromTypeEnum.PLAN || runPlanBean.getFromTypeEnum() == PlanRunFromTypeEnum.CRON_JOB) {
            snapshot.setMaxOccurs(autoPlan.getMaxOccurs());
            snapshot.setFailContinue(autoPlan.getFailContinue());
            if(runPlanBean.getPlanVariables() == null || runPlanBean.getPlanVariables().isEmpty()) {
                snapshot.setPlanVariables(autoPlan.getPlanVariables());
            }else{
                snapshot.setPlanVariables(TaskService.gson.toJson(runPlanBean.getPlanVariables()));
            }
        }
        snapshot.setRunType(runPlanBean.getRunType());

        if(snapshot.getEnvId() != null) {
            RunEnv runEnv = runEnvMapper.findById(snapshot.getEnvId());
            if(runEnv != null) {
                snapshot.setEnvName(runEnv.getName());
            }
        }

        planRunningConfigSnapshotMapper.insertBySelective(snapshot);
        return snapshot;
    }

    private PlanExecuteResult newPlanExecuteResult(RunPlanBean bean, User loginUser) {
        DataNode dataNode;
        if(bean.getFromTypeEnum() == PlanRunFromTypeEnum.PLAN || bean.getFromTypeEnum() == PlanRunFromTypeEnum.CRON_JOB) {
            dataNode = dataNodeMapper.selectByPrimaryKey(bean.getPlanId(), DataTypeEnum.AUTO_PLAN.value());
        }else{
            dataNode = dataNodeMapper.selectByPrimaryKey(bean.getCaseId(), DataTypeEnum.AUTO_CASE.value());
        }
        if(dataNode == null) {
            return null;
        }
        return addPlanExecuteResult(dataNode, loginUser, bean);
    }

    private PlanExecuteResult addPlanExecuteResult(DataNode dataNode, User loginUser, RunPlanBean runPlanBean) {
        PlanExecuteResult record = new PlanExecuteResult();
        record.setFromType(runPlanBean.getFromTypeEnum().value());
        record.setPlanOrCaseId(dataNode.getId());
        record.setPlanOrCaseName(dataNode.getName());
        if(PlanRunFromTypeEnum.CRON_JOB == runPlanBean.getFromTypeEnum()) {
            record.setSubmitter("定时任务");
        }else {
            record.setSubmitter(loginUser.getUsername());
        }
        record.setSubmitTimestamp(System.currentTimeMillis());
        record.setWorkerIp(LocalHostUtils.getLocalIp());
        record.setPlanCronJobId(runPlanBean.getPlanCronJobId());
        record.setSubmitDate(DateUtils.parseTimestampToFormatDate(System.currentTimeMillis(), DateUtils.DATE_PATTERN_YMD));
        record.setTotal(1);
        record.setRunDescription(runPlanBean.getRunDescription());

        planExecuteResultMapper.insertBySelective(record);
        return record;
    }

    public BaseResponse getPlanRunResultStatus(GetPlanRunResultStatusBody body, User user) {
        log.info("{} 查询计划结果执行状态", user.getUsername());
        PlanExecuteResult planExecuteResult = planExecuteResultMapper.selectByPrimaryId(body.getPlanResultId());
        return ResultUtils.success(planExecuteResult);
    }
}
