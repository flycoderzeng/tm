package com.tm.web.service;

import com.tm.common.base.mapper.*;
import com.tm.common.base.model.AutoPlan;
import com.tm.common.base.model.DataNode;
import com.tm.common.base.model.PlanCase;
import com.tm.common.base.model.User;
import com.tm.common.entities.autotest.request.SaveNodeBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.common.enumerate.DataTypeEnum;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("autoPlanService")
public class AutoPlanService extends BaseService {

    @Autowired
    private AutoPlanMapper autoPlanMapper;
    @Autowired
    private DataNodeMapper dataNodeMapper;
    @Autowired
    private PlanCaseMapper planCaseMapper;
    @Autowired
    private PlanCaseSetupMapper planCaseSetupMapper;
    @Autowired
    private PlanCaseTeardownMapper planCaseTeardownMapper;

    public BaseResponse copy(SaveNodeBody body) {
        AutoPlan autoPlan = autoPlanMapper.selectByPrimaryId(body.getCopyId());
        if(autoPlan == null) {
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        autoPlan.setId(body.getId());
        autoPlanMapper.updateBySelective(autoPlan);

        CommonTableQueryBody queryBody = new CommonTableQueryBody();
        queryBody.setPlanId(body.getCopyId());
        queryBody.setOffset(0);
        queryBody.setPageSize(10000);
        queryBody.setOrder("seq");
        queryBody.setSort("asc");
        List<PlanCase> planCases = planCaseMapper.queryList(queryBody);
        if(planCases != null && !planCases.isEmpty()) {
            for (PlanCase planCase : planCases) {
                planCase.setPlanId(autoPlan.getId());
                planCase.setId(null);
            }
            planCaseMapper.batchInsert(planCases);
        }
        List<PlanCase> planCases1 = planCaseSetupMapper.queryList(queryBody);
        if(planCases1 != null && !planCases1.isEmpty()) {
            for (PlanCase planCase : planCases1) {
                planCase.setPlanId(autoPlan.getId());
                planCase.setId(null);
            }
            planCaseSetupMapper.batchInsert(planCases1);
        }
        List<PlanCase> planCases2 = planCaseTeardownMapper.queryList(queryBody);
        if(planCases2 != null && !planCases2.isEmpty()) {
            for (PlanCase planCase : planCases2) {
                planCase.setPlanId(autoPlan.getId());
                planCase.setId(null);
            }
            planCaseTeardownMapper.batchInsert(planCases2);
        }

        return ResultUtils.success();
    }

    public BaseResponse load(Integer id) {
        AutoPlan autoPlan = autoPlanMapper.selectByPrimaryId(id);
        DataNode dataNode = dataNodeMapper.selectByPrimaryKey(id, DataTypeEnum.AUTO_PLAN.value());
        setDataNodeInfo(autoPlan, dataNode);

        return ResultUtils.success(autoPlan);
    }

    public BaseResponse save(AutoPlan autoPlan, User user) {
        if(autoPlan.getId() != null && autoPlan.getId() > 0) {
            autoPlanMapper.updateBySelective(autoPlan);
        }else{
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
        return updateNode4CommonFields(user, autoPlan, DataTypeEnum.AUTO_PLAN);
    }

    public BaseResponse savePlanVariables(AutoPlan autoPlan, User user) {
        if(autoPlan.getId() != null && autoPlan.getId() > 0) {
            autoPlanMapper.updateBySelective(autoPlan);
            return ResultUtils.success();
        }else{
            return ResultUtils.error(ResultCodeEnum.PARAM_ERROR);
        }
    }
}
