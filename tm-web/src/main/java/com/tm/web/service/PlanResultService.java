package com.tm.web.service;

import com.tm.common.base.model.CaseExecuteResult;
import com.tm.common.base.model.CaseStepExecuteResult;
import com.tm.common.base.model.CaseVariableValueResult;
import com.tm.common.base.model.PlanExecuteResult;
import com.tm.common.dao.PlanExecuteResultDao;
import com.tm.common.entities.autotest.enumerate.PlanCaseEnum;
import com.tm.common.entities.autotest.request.GetNewestPlanExecuteResultBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import com.tm.common.utils.TableSuffixUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service(value = "planResultService")
public class PlanResultService {

    @Value("${spring.autotest.result.case-result-split-table-type}")
    private Integer splitCaseResultTableType;
    @Value("${spring.autotest.result.variable-result-split-table-type}")
    private Integer splitVariableTableType;
    @Value("${spring.autotest.result.case-step-result-split-table-type}")
    private Integer splitCaseStepResultTableType;

    @Autowired
    private PlanExecuteResultDao planExecuteResultDao;

    public BaseResponse getNewestPlanExecuteResult(GetNewestPlanExecuteResultBody body) {
        PlanExecuteResult executeResult = planExecuteResultDao.getNewestPlanExecuteResult(body);
        return ResultUtils.success(executeResult);
    }

    public BaseResponse getPlanHistoryExecuteResultList(CommonTableQueryBody body) {
        if(StringUtils.isBlank(body.getOrder())) {
            body.setOrder("id");
        }
        body.setPlanCaseType(PlanCaseEnum.DEFAULT.value());
        CommonTableQueryResponse response = new CommonTableQueryResponse<PlanExecuteResult>();
        Integer c = planExecuteResultDao.getPlanHistoryExecuteResultListCount(body);
        response.setTotal(c);
        if(c != null && c > 0) {
            List<PlanExecuteResult> results = planExecuteResultDao.getPlanHistoryExecuteResultList(body);
            response.setRows(results);
        }
        return ResultUtils.success(response);
    }

    public BaseResponse getSinglePlanExecuteResult(Integer id) {
        PlanExecuteResult executeResult = planExecuteResultDao.selectByPrimaryId(id);
        return ResultUtils.success(executeResult);
    }

    public BaseResponse getPlanCaseExecuteResultList(CommonTableQueryBody body) {
        PlanExecuteResult planExecuteResult = planExecuteResultDao.selectByPrimaryId(body.getPlanResultId());
        if(planExecuteResult == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        body.setTableSuffix(TableSuffixUtils.getTableSuffix(new Date(planExecuteResult.getStartTimestamp()),
                splitCaseResultTableType, 0));
        if(StringUtils.isBlank(body.getOrder())) {
            body.setOrder("result_status");
        }
        CommonTableQueryResponse response = new CommonTableQueryResponse<CaseExecuteResult>();
        Integer c = planExecuteResultDao.getPlanCaseExecuteResultListCount(body);
        response.setTotal(c);
        if(c != null && c > 0) {
            List<CaseExecuteResult> results = planExecuteResultDao.getPlanCaseExecuteResultList(body);
            response.setRows(results);
        }
        return ResultUtils.success(response);
    }

    public BaseResponse getCaseVariableResultList(CommonTableQueryBody body) {
        PlanExecuteResult planExecuteResult = planExecuteResultDao.selectByPrimaryId(body.getPlanResultId());
        if(planExecuteResult == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        body.setTableSuffix(TableSuffixUtils.getTableSuffix(new Date(planExecuteResult.getStartTimestamp()),
                splitVariableTableType, 0));
        List<CaseVariableValueResult> results = planExecuteResultDao.getCaseVariableResultList(body);

        return ResultUtils.success(results);
    }

    public BaseResponse getCaseStepResultList(CommonTableQueryBody body) {
        PlanExecuteResult planExecuteResult = planExecuteResultDao.selectByPrimaryId(body.getPlanResultId());
        if(planExecuteResult == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        body.setTableSuffix(TableSuffixUtils.getTableSuffix(new Date(planExecuteResult.getStartTimestamp()),
                splitVariableTableType, 0));
        if(StringUtils.isBlank(body.getOrder())) {
            body.setOrder("id");
        }

        CommonTableQueryResponse response = new CommonTableQueryResponse<CaseStepExecuteResult>();
        Integer c = planExecuteResultDao.getCaseStepResultListCount(body);
        response.setTotal(c);
        if(c != null && c > 0) {
            List<CaseStepExecuteResult> results = planExecuteResultDao.getCaseStepResultList(body);
            response.setRows(results);
        }
        return ResultUtils.success(response);
    }
}
