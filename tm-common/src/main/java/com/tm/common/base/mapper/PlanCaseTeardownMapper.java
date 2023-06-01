package com.tm.common.base.mapper;


import com.tm.common.base.model.PlanCase;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanCaseTeardownMapper {
    int insertBySelective(PlanCase planCase);
    int updateBySelective(PlanCase planCase);
    int batchInsert(@Param("planCaseList") List<PlanCase> planCaseList);
    int deleteByPlanId(@Param("planId") Integer planId);
    List<PlanCase> selectByPlanIdAndCaseId(@Param("planId") Integer planId, @Param("caseId") Integer caseId);
    int deleteByPrimaryKeyList(@Param("planId") Integer planId, @Param("idList") List<Integer> idList);
    List<PlanCase> queryList(CommonTableQueryBody body);
    int countList(CommonTableQueryBody body);
    Integer selectMaxSeq(@Param("planId") Integer planId);
    int increaseSeq(@Param("planId") Integer planId, @Param("baseSeq") Integer baseSeq);
}
