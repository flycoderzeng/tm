package com.tm.common.base.model;

import lombok.Data;

@Data
public class CaseVariableValueResult {
    private Integer id;
    private Integer planResultId;
    private Integer caseId;
    private Integer groupNo;
    private String variableName;
    private String variableValue;
    private String tableSuffix;
}
