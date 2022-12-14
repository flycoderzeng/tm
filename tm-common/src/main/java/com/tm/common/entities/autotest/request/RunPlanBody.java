package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RunPlanBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
    @NotNull(message = "运行类型不能为空")
    private Integer runType;
    @Min(0)
    private Integer priority = 0;
}
