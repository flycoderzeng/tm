package com.tm.common.base.mapper;

import com.tm.common.base.model.PlanCronJob;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface PlanCronJobMapper {

    int deleteByPrimaryKey(int id);

    PlanCronJob findById(int id);

    List<PlanCronJob> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);

    int updateBySelective(PlanCronJob record);

    int insertBySelective(PlanCronJob record);
}
