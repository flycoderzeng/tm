package com.tm.common.base.model;

import lombok.Data;

@Data
public class CaseStepExecuteResult {
    private Integer id;
    private Integer planResultId;
    private Integer caseId;
    private Integer groupNo;
    private Long startTimestamp;
    private Long endTimestamp;
    private Integer resultStatus;
    private String stepKey;
    private String name;
    private String description;
    private String resultInfo;
    private String tableSuffix;
}
