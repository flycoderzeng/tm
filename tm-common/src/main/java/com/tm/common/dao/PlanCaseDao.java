package com.tm.common.dao;

import com.tm.common.base.mapper.PlanCaseMapper;
import com.tm.common.base.model.PlanCase;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PlanCaseDao {
    @Autowired
    private PlanCaseMapper planCaseMapper;

    public List<PlanCase> queryList(CommonTableQueryBody body) {
        return planCaseMapper.queryList(body);
    }

    public int countList(CommonTableQueryBody body) {
        return planCaseMapper.countList(body);
    }
}
