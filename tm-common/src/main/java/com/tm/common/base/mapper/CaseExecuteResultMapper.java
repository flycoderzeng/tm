package com.tm.common.base.mapper;

import com.tm.common.base.model.CaseExecuteResult;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaseExecuteResultMapper {
    int insertBySelective(CaseExecuteResult record);
    int updateBySelective(CaseExecuteResult record);
    List<CaseExecuteResult> selectByPlanResultIdCaseIdAndGroupNo(@Param("planResultId") Integer planResultId,
                                                                      @Param("caseId") Integer caseId,
                                                                      @Param("groupNo") Integer groupNo,
                                                                       @Param("tableSuffix") String tableSuffix);
    void createCaseExecuteResultTable(String tableSuffix);

    List<CaseExecuteResult> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);

    List<CaseExecuteResult> getExecuteSuccessCaseResultList(@Param("planResultId") Integer planResultId,
                                                            @Param("tableSuffix") String tableSuffix);

    int deleteFailedCaseResult(@Param("planResultId") Integer planResultId,
                                                            @Param("tableSuffix") String tableSuffix);
}
