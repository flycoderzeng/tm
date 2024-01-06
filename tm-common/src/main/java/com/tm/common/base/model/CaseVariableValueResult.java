package com.tm.common.base.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseVariableValueResult implements Serializable {
    private Integer id;
    private Integer planResultId;
    private Integer caseId;
    private Integer groupNo;
    private String variableName;
    private String variableValue;
    private String tableSuffix;
}
