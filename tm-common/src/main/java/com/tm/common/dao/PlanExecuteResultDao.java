package com.tm.common.dao;


import com.tm.common.base.mapper.CaseExecuteResultMapper;
import com.tm.common.base.mapper.CaseStepExecuteResultMapper;
import com.tm.common.base.mapper.CaseVariableValueResultMapper;
import com.tm.common.base.mapper.PlanExecuteResultMapper;
import com.tm.common.base.model.CaseExecuteResult;
import com.tm.common.base.model.CaseStepExecuteResult;
import com.tm.common.base.model.CaseVariableValueResult;
import com.tm.common.base.model.PlanExecuteResult;
import com.tm.common.entities.autotest.enumerate.PlanExecuteResultStatusEnum;
import com.tm.common.entities.autotest.request.GetNewestPlanExecuteResultBody;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanExecuteResultDao {
    @Autowired
    private PlanExecuteResultMapper planExecuteResultMapper;
    @Autowired
    private CaseExecuteResultMapper caseExecuteResultMapper;
    @Autowired
    private CaseVariableValueResultMapper caseVariableValueResultMapper;
    @Autowired
    private CaseStepExecuteResultMapper caseStepExecuteResultMapper;

    public void insertBySelective(PlanExecuteResult record) {
        planExecuteResultMapper.insertBySelective(record);
    }

    public void updateBySelective(PlanExecuteResult record) {
        planExecuteResultMapper.updateBySelective(record);
    }

    public PlanExecuteResult selectByPrimaryId(Integer id) {
        return planExecuteResultMapper.selectByPrimaryId(id);
    }

    public void setPlanExecuteResultEndStatus(PlanExecuteResult planExecuteResult,
                                              PlanExecuteResultStatusEnum status,
                                              String resultInfo) {
        planExecuteResult.setResultStatus(status.value());
        planExecuteResult.setResultInfo(resultInfo);
        planExecuteResult.setEndTimestamp(System.currentTimeMillis());
        planExecuteResultMapper.updateBySelective(planExecuteResult);
    }

    public void setPlanExecuteEnd(PlanExecuteResult planExecuteResult) {
        planExecuteResult.setResultStatus(PlanExecuteResultStatusEnum.FINISHED.value());
        planExecuteResult.setEndTimestamp(System.currentTimeMillis());
        planExecuteResultMapper.updateBySelective(planExecuteResult);
    }

    public void setPlanExecuteResultStatus(PlanExecuteResult planExecuteResult,
                                           PlanExecuteResultStatusEnum status) {
        planExecuteResult.setResultStatus(status.value());
        planExecuteResult.setEndTimestamp(System.currentTimeMillis());
        planExecuteResultMapper.updateBySelective(planExecuteResult);
    }

    public void setTotal(PlanExecuteResult planExecuteResult, int total) {
        planExecuteResult.setTotal(total);
        planExecuteResultMapper.updateBySelective(planExecuteResult);
    }

    public void setPlanExecuteResultStartTimestamp(PlanExecuteResult planExecuteResult) {
        planExecuteResult.setStartTimestamp(System.currentTimeMillis());
        planExecuteResultMapper.updateBySelective(planExecuteResult);
    }

    public void setFailCount(Integer planExecuteResultId, Integer failCount) {
        planExecuteResultMapper.setFailCount(planExecuteResultId, failCount);
    }

    public void setSuccessCount(Integer planExecuteResultId, Integer successCount) {
        planExecuteResultMapper.setSuccessCount(planExecuteResultId, successCount);
    }

    public PlanExecuteResult getNewestPlanExecuteResult(GetNewestPlanExecuteResultBody body) {
        return planExecuteResultMapper.getNewestPlanExecuteResult(body);
    }

    public List<PlanExecuteResult> getPlanHistoryExecuteResultList(CommonTableQueryBody body) {
        return planExecuteResultMapper.getPlanHistoryExecuteResultList(body);
    }

    public Integer getPlanHistoryExecuteResultListCount(CommonTableQueryBody body) {
        return planExecuteResultMapper.getPlanHistoryExecuteResultListCount(body);
    }

    public Integer getPlanCaseExecuteResultListCount(CommonTableQueryBody body) {
        return caseExecuteResultMapper.countList(body);
    }

    public List<CaseExecuteResult> getPlanCaseExecuteResultList(CommonTableQueryBody body) {
        return caseExecuteResultMapper.queryList(body);
    }

    public List<CaseVariableValueResult> getCaseVariableResultList(CommonTableQueryBody body) {
        return caseVariableValueResultMapper.selectByPlanResultIdCaseIdAndGroupNo(body);
    }

    public Integer getCaseStepResultListCount(CommonTableQueryBody body) {
        return caseStepExecuteResultMapper.countList(body);
    }

    public List<CaseStepExecuteResult> getCaseStepResultList(CommonTableQueryBody body) {
        return caseStepExecuteResultMapper.queryList(body);
    }

    public List<CaseExecuteResult> getExecuteSuccessCaseResultList(int planResultId, String tableSuffix) {
        return caseExecuteResultMapper.getExecuteSuccessCaseResultList(planResultId, tableSuffix);
    }

    public int deleteFailedCaseResult(int planResultId, String tableSuffix) {
        return caseExecuteResultMapper.deleteFailedCaseResult(planResultId, tableSuffix);
    }

    public int deleteFailedCaseStepResult(Integer planResultId, Integer caseId, Integer groupNo, String tableSuffix) {
        return caseStepExecuteResultMapper.deleteFailedCaseStepResult(planResultId, caseId, groupNo, tableSuffix);
    }

    public int deleteFailedCaseVariableResult(Integer planResultId, Integer caseId, Integer groupNo, String tableSuffix) {
        return caseVariableValueResultMapper.deleteFailedCaseVariableResult(planResultId, caseId, groupNo, tableSuffix);
    }
}
