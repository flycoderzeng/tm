package com.tm.web.service;

import com.tm.common.base.mapper.PlanCaseMapper;
import com.tm.common.base.mapper.PlanCaseSetupMapper;
import com.tm.common.base.mapper.PlanCaseTeardownMapper;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.PlanCase;
import com.tm.common.entities.autotest.request.AddCaseToPlanBody;
import com.tm.common.entities.autotest.request.ClearPlanCaseBody;
import com.tm.common.entities.autotest.request.DeletePlanCaseBody;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "planCaseService")
public class PlanCaseService {
    @Resource
    private PlanCaseMapper planCaseMapper;
    @Resource
    private PlanCaseSetupMapper planCaseSetupMapper;
    @Resource
    private PlanCaseTeardownMapper planCaseTeardownMapper;


    public CommonTableQueryResponse queryList(CommonTableQueryBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        if(body.getPlanCaseType() == 0) {
            response.setRows(planCaseMapper.queryList(body));
            response.setTotal(planCaseMapper.countList(body));
        }else if(body.getPlanCaseType() == 1) {
            response.setRows(planCaseSetupMapper.queryList(body));
            response.setTotal(planCaseSetupMapper.countList(body));
        }else if(body.getPlanCaseType() == 2) {
            response.setRows(planCaseTeardownMapper.queryList(body));
            response.setTotal(planCaseTeardownMapper.countList(body));
        }
        return response;
    }

    public void addCaseToPlan(AddCaseToPlanBody body) {
        if(body.getCaseIdList() == null || body.getCaseIdList().isEmpty()) {
            return ;
        }
        List<Integer> notExistCaseIdList = new ArrayList<>();
        List<Integer> repeatedIdList = new ArrayList<>();
        for (int i = 0; i < body.getCaseIdList().size(); i++) {
            Integer caseId = body.getCaseIdList().get(i);
            List<PlanCase> planCases = null;
            if(body.getType() == 0) {
                planCases = planCaseMapper.selectByPlanIdAndCaseId(body.getPlanId(), caseId);
            }else if(body.getType() == 1) {
                planCases = planCaseSetupMapper.selectByPlanIdAndCaseId(body.getPlanId(), caseId);
            }else if(body.getType() == 2) {
                planCases = planCaseTeardownMapper.selectByPlanIdAndCaseId(body.getPlanId(), caseId);
            }
            if(planCases == null || planCases.isEmpty()) {
                notExistCaseIdList.add(caseId);
            }else{
                if(planCases.size() > 1) {
                    for (int i1 = 1; i1 < planCases.size(); i1++) {
                        repeatedIdList.add(planCases.get(i).getId());
                    }
                }
            }
        }
        List<PlanCase> planCaseList = new ArrayList<>();
        Integer seq = null;
        if(body.getType() == 0) {
            seq = planCaseMapper.selectMaxSeq(body.getPlanId());
        }else if(body.getType() == 1) {
            seq = planCaseSetupMapper.selectMaxSeq(body.getPlanId());
        }else if(body.getType() == 2) {
            seq = planCaseTeardownMapper.selectMaxSeq(body.getPlanId());
        }
        if(seq == null) {
            seq = 0;
        }
        for (int i = 0; i < notExistCaseIdList.size(); i++) {
            planCaseList.add(new PlanCase(body.getPlanId(), notExistCaseIdList.get(i), seq+1));
            seq++;
        }
        if(!planCaseList.isEmpty()) {
            if(body.getType() == 0) {
                planCaseMapper.batchInsert(planCaseList);
            }else if(body.getType() == 1) {
                planCaseSetupMapper.batchInsert(planCaseList);
            }else if(body.getType() == 2) {
                planCaseTeardownMapper.batchInsert(planCaseList);
            }
        }
        if(repeatedIdList != null && !repeatedIdList.isEmpty()) {
            if(body.getType() == 0) {
                planCaseMapper.deleteByPrimaryKeyList(body.getPlanId(), repeatedIdList);
            }else if(body.getType() == 1) {
                planCaseSetupMapper.deleteByPrimaryKeyList(body.getPlanId(), repeatedIdList);
            }else if(body.getType() == 1) {
                planCaseTeardownMapper.deleteByPrimaryKeyList(body.getPlanId(), repeatedIdList);
            }
        }
    }

    public void deletePlanCase(DeletePlanCaseBody body) {
        if(body.getIdList() == null || body.getIdList().isEmpty()) {
            return ;
        }
        if(body.getType() == 0) {
            planCaseMapper.deleteByPrimaryKeyList(body.getPlanId(), body.getIdList());
        }else if (body.getType() == 1) {
            planCaseSetupMapper.deleteByPrimaryKeyList(body.getPlanId(), body.getIdList());
        }else if (body.getType() == 2) {
            planCaseTeardownMapper.deleteByPrimaryKeyList(body.getPlanId(), body.getIdList());
        }
    }

    public void clearPlanCase(ClearPlanCaseBody body) {
        if(body.getType() == 0) {
            planCaseMapper.deleteByPlanId(body.getPlanId());
        }else if (body.getType() == 1) {
            planCaseSetupMapper.deleteByPlanId(body.getPlanId());
        }else if (body.getType() == 2) {
            planCaseTeardownMapper.deleteByPlanId(body.getPlanId());
        }
    }

    public void changeCaseSeq(Integer planId, Integer caseId, Integer seq, Integer type) {
        if(type == 0) {
            planCaseMapper.increaseSeq(planId, seq);
        }else if(type == 1) {
            planCaseSetupMapper.increaseSeq(planId, seq);
        }else if(type == 2) {
            planCaseTeardownMapper.increaseSeq(planId, seq);
        }
        if(type == 0) {
            List<PlanCase> planCases = planCaseMapper.selectByPlanIdAndCaseId(planId, caseId);
            if(planCases != null && !planCases.isEmpty()) {
                PlanCase planCase = planCases.get(0);
                planCase.setSeq(seq);
                planCaseMapper.updateBySelective(planCase);
            }
        }else if(type == 1) {
            List<PlanCase> planCases = planCaseSetupMapper.selectByPlanIdAndCaseId(planId, caseId);
            if(planCases != null && !planCases.isEmpty()) {
                PlanCase planCase = planCases.get(0);
                planCase.setSeq(seq);
                planCaseSetupMapper.updateBySelective(planCase);
            }
        }else if(type == 2) {
            List<PlanCase> planCases = planCaseTeardownMapper.selectByPlanIdAndCaseId(planId, caseId);
            if(planCases != null && !planCases.isEmpty()) {
                PlanCase planCase = planCases.get(0);
                planCase.setSeq(seq);
                planCaseTeardownMapper.updateBySelective(planCase);
            }
        }
    }
}
