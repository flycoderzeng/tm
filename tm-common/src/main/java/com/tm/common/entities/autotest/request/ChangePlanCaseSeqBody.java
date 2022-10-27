package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePlanCaseSeqBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
    @NotNull(message = "用例id不能为空")
    private Integer caseId;
    @NotNull(message = "序号不能为空")
    private Integer seq;
}
