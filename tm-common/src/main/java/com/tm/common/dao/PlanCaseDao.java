package com.tm.common.dao;

import com.tm.common.base.mapper.PlanCaseMapper;
import com.tm.common.base.mapper.PlanCaseSetupMapper;
import com.tm.common.base.mapper.PlanCaseTeardownMapper;
import com.tm.common.base.model.PlanCase;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PlanCaseDao {
    @Autowired
    private PlanCaseMapper planCaseMapper;
    @Autowired
    private PlanCaseSetupMapper planCaseSetupMapper;
    @Autowired
    private PlanCaseTeardownMapper planCaseTeardownMapper;

    public List<PlanCase> queryCaseList(CommonTableQueryBody body) {
        return planCaseMapper.queryList(body);
    }

    public int countCaseList(CommonTableQueryBody body) {
        return planCaseMapper.countList(body);
    }

    public List<PlanCase> querySetupCaseList(CommonTableQueryBody body) {
        return planCaseSetupMapper.queryList(body);
    }

    public int countSetupCaseList(CommonTableQueryBody body) {
        return planCaseSetupMapper.countList(body);
    }

    public List<PlanCase> queryTeardownCaseList(CommonTableQueryBody body) {
        return planCaseTeardownMapper.queryList(body);
    }

    public int countTeardownCaseList(CommonTableQueryBody body) {
        return planCaseTeardownMapper.countList(body);
    }
}
