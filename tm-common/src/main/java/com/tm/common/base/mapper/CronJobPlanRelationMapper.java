package com.tm.common.base.mapper;

import com.tm.common.base.model.CronJobPlanRelation;

import java.util.List;

public interface CronJobPlanRelationMapper {
    int insertBySelective(CronJobPlanRelation record);
    int updateBySelective(CronJobPlanRelation record);
    List<CronJobPlanRelation> selectByCronJobId(Integer cronJobId);
    int deleteByPrimaryKey(Integer id);
}
