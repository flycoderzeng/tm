package com.tm.common.base.model;

import lombok.Data;

@Data
public class CaseExecuteResult extends Common5ItemsModel {
    private Integer planResultId;
    private Integer caseId;
    private Integer groupNo;
    private String groupName;
    private Long startTimestamp;
    private Long endTimestamp;
    private Integer resultStatus;
    private Integer seq = 1;
    private String workerIp;
    private String name;
    private String description;
    private String resultInfo;
    private String errorStepKey;
    private String steps;
    private String tableSuffix;
}
