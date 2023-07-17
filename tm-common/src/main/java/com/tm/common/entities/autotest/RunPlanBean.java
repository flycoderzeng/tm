package com.tm.common.entities.autotest;

import com.tm.common.entities.autotest.enumerate.PlanRunFromTypeEnum;
import com.tm.common.entities.common.KeyValueRow;
import lombok.Data;

import java.util.List;

@Data
public class RunPlanBean {
    private Integer caseId;
    private Integer planId;

    private Integer runType;
    private Integer runEnvId;
    private Integer planCronJobId;
    private Integer priority = 0;
    private PlanRunFromTypeEnum fromTypeEnum = PlanRunFromTypeEnum.CASE;
    private List<KeyValueRow> planVariables;
    private String runDescription;
}
