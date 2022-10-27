package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlanCronJob extends Common6ItemsModel {
    private String name;
    private String description;
    private String cronExpression;
    private Date lastRunTime;

    private List<CronJobPlanRelation> planList;
}
