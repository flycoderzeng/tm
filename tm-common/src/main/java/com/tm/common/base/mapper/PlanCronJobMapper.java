package com.tm.common.base.mapper;


import com.tm.common.base.model.PlanCronJob;

import java.util.List;

public interface PlanCronJobMapper {
    List<PlanCronJob> getAllPlanCronJobs();
    int updateBySelective(PlanCronJob record);

    PlanCronJob selectByPrimaryKey(Integer id);
}
