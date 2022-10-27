package com.tm.common.base.mapper;

import com.tm.common.base.model.CaseStepExecuteResult;
import com.tm.common.entities.base.CommonTableQueryBody;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaseStepExecuteResultMapper {
    int insertBySelective(CaseStepExecuteResult record);
    int updateBySelective(CaseStepExecuteResult record);
    List<CaseStepExecuteResult> selectByPlanResultIdCaseIdAndGroupNo(@Param("planResultId") Integer planResultId,
                                                                      @Param("caseId") Integer caseId,
                                                                      @Param("groupNo") Integer groupNo,
                                                                       @Param("tableSuffix") String tableSuffix);
    void createCaseStepExecuteResultTable(String tableSuffix);



    List<CaseStepExecuteResult> queryList(CommonTableQueryBody body);

    int countList(CommonTableQueryBody body);
}
