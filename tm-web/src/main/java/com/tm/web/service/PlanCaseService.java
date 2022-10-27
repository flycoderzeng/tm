package com.tm.web.service;

import com.tm.common.base.mapper.PlanCaseMapper;
import com.tm.common.base.model.Menu;
import com.tm.common.base.model.PlanCase;
import com.tm.common.entities.autotest.request.AddCaseToPlanBody;
import com.tm.common.entities.base.CommonTableQueryBody;
import com.tm.common.entities.base.CommonTableQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "planCaseService")
public class PlanCaseService {
    @Autowired
    private PlanCaseMapper planCaseMapper;

    public CommonTableQueryResponse queryList(CommonTableQueryBody body) {
        CommonTableQueryResponse response = new CommonTableQueryResponse<Menu>();
        response.setRows(planCaseMapper.queryList(body));
        response.setTotal(planCaseMapper.countList(body));
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
            List<PlanCase> planCases = planCaseMapper.selectByPlanIdAndCaseId(body.getPlanId(), caseId);
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
        Integer seq = planCaseMapper.selectMaxSeq(body.getPlanId());
        if(seq == null) {
            seq = 0;
        }
        for (int i = 0; i < notExistCaseIdList.size(); i++) {
            planCaseList.add(new PlanCase(body.getPlanId(), notExistCaseIdList.get(i), seq+1));
            seq++;
        }
        if(!planCaseList.isEmpty()) {
            planCaseMapper.batchInsert(planCaseList);
        }
        if(repeatedIdList != null && !repeatedIdList.isEmpty()) {
            planCaseMapper.deleteByPrimaryKeyList(body.getPlanId(), repeatedIdList);
        }
    }

    public void deletePlanCase(Integer planId, List<Integer> idList) {
        if(idList == null || idList.isEmpty()) {
            return ;
        }
        planCaseMapper.deleteByPrimaryKeyList(planId, idList);
    }

    public void clearPlanCase(Integer planId) {
        planCaseMapper.deleteByPlanId(planId);
    }

    public void changeCaseSeq(Integer planId, Integer caseId, Integer seq) {
        planCaseMapper.increaseSeq(planId, seq);
        List<PlanCase> planCases = planCaseMapper.selectByPlanIdAndCaseId(planId, caseId);
        if(planCases != null && !planCases.isEmpty()) {
            PlanCase planCase = planCases.get(0);
            planCase.setSeq(seq);
            planCaseMapper.updateBySelective(planCase);
        }
    }
}
