package com.tm.common.base.model;

import lombok.Data;

@Data
public class PlanExecuteResult {
    private Integer id;
    private Integer planOrCaseId;
    private String planOrCaseName;
    private Integer resultStatus;
    private Long startTimestamp;
    private Long endTimestamp;
    private Integer total;
    private Integer successCount;
    private Integer failCount;
    private String resultInfo;
    private String submitter;
    private Long submitTimestamp;
    private Integer mailSent;
    private String workerIp;
    private Integer planCronJobId;
    private Integer fromType;
    private String submitDate;
}
