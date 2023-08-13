package com.tm.common.base.mapper;

import com.tm.common.base.model.CaseVariableValueResult;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaseVariableValueResultMapper {
    int insertBySelective(CaseVariableValueResult record);
    int updateBySelective(CaseVariableValueResult record);
    List<CaseVariableValueResult> selectByPlanResultIdCaseIdAndGroupNo(CommonTableQueryBody body);
    void createCaseVariableValueResultTable(String tableSuffix);

    int deleteFailedCaseVariableResult(@Param("planResultId") Integer planResultId,
                                       @Param("caseId") Integer caseId,
                                       @Param("groupNo") Integer groupNo,
                                       @Param("tableSuffix") String tableSuffix);
}
