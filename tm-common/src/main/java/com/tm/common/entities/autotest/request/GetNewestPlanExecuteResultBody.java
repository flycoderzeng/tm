package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GetNewestPlanExecuteResultBody {
    @NotNull(message = "计划或用例id不能为空")
    private Integer planOrCaseId;
    //1-计划2-用例3-定时计划
    @NotNull(message = "计划运行来源类型不能为空")
    @Min(1)
    @Max(3)
    private Integer fromType;
}
