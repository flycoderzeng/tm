package com.tm.common.entities.autotest;

import com.tm.common.entities.autotest.enumerate.PlanRunFromTypeEnum;
import lombok.Data;

@Data
public class RunPlanBean {
    private Integer caseId;
    private Integer planId;

    private Integer runType;
    private Integer runEnvId;
    private Integer planCronJobId;
    private Integer priority = 0;
    private PlanRunFromTypeEnum fromTypeEnum = PlanRunFromTypeEnum.CASE;
}
