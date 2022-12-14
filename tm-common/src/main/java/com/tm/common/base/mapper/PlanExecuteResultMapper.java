package com.tm.common.base.mapper;

import com.tm.common.base.model.PlanExecuteResult;
import com.tm.common.entities.autotest.request.GetNewestPlanExecuteResultBody;
import com.tm.common.entities.base.CommonTableQueryBody;

import java.util.List;

public interface PlanExecuteResultMapper {
    int insertBySelective(PlanExecuteResult record);
    int updateBySelective(PlanExecuteResult record);
    PlanExecuteResult selectByPrimaryId(Integer id);

    void addFailCount(Integer planExecuteResultId);

    void addSuccessCount(Integer planExecuteResultId);

    PlanExecuteResult getNewestPlanExecuteResult(GetNewestPlanExecuteResultBody body);

    List<PlanExecuteResult> getPlanHistoryExecuteResultList(CommonTableQueryBody body);

    Integer getPlanHistoryExecuteResultListCount(CommonTableQueryBody body);
}
