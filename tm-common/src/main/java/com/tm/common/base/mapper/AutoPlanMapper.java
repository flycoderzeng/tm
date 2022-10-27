package com.tm.common.base.mapper;

import com.tm.common.base.model.AutoPlan;

public interface AutoPlanMapper {
    int insertBySelective(AutoPlan record);
    int updateBySelective(AutoPlan record);
    AutoPlan selectByPrimaryId(Integer id);
}
