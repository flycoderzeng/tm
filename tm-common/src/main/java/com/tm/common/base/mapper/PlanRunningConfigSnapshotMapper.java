package com.tm.common.base.mapper;

import com.tm.common.base.model.PlanRunningConfigSnapshot;

public interface PlanRunningConfigSnapshotMapper {
    int insertBySelective(PlanRunningConfigSnapshot record);
    int updateBySelective(PlanRunningConfigSnapshot record);
    PlanRunningConfigSnapshot selectByPrimaryPlanResultId(Integer id);
}
