package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RetryFailedCaseBody {
    @NotNull(message = "计划结果id不能为空")
    private Integer planResultId;
}
