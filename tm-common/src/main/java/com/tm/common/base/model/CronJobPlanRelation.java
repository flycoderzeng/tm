package com.tm.common.base.model;

import lombok.Data;

@Data
public class CronJobPlanRelation {
    private Integer id;
    private Integer planCronJobId;
    private Integer planId;
    private Integer envId;
    private Integer runType;
    private String planName;
    private String envName;
    private Integer status;
}
