package com.tm.common.dao;

import com.tm.common.base.mapper.PlanCaseMapper;
import com.tm.common.base.mapper.PlanCaseSetupMapper;
import com.tm.common.base.mapper.PlanCaseTeardownMapper;
import com.tm.common.base.model.PlanCase;
import com.tm.common.entities.base.CommonTableQueryBody;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;


@Component
public class PlanCaseDao {
    private final PlanCaseMapper planCaseMapper;
    private final PlanCaseSetupMapper planCaseSetupMapper;
    private final PlanCaseTeardownMapper planCaseTeardownMapper;

    @Inject
    public PlanCaseDao(PlanCaseMapper planCaseMapper, PlanCaseSetupMapper planCaseSetupMapper,
                       PlanCaseTeardownMapper planCaseTeardownMapper) {
        Assert.notNull(planCaseMapper, "PlanCaseMapper must not be null!");
        Assert.notNull(planCaseSetupMapper, "PlanCaseSetupMapper must not be null!");
        Assert.notNull(planCaseTeardownMapper, "PlanCaseTeardownMapper must not be null!");
        this.planCaseMapper = planCaseMapper;
        this.planCaseSetupMapper = planCaseSetupMapper;
        this.planCaseTeardownMapper = planCaseTeardownMapper;
    }

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
