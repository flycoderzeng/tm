package com.tm.common.entities.autotest.request;

import com.tm.common.retention.EnumValue;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ChangePlanCaseSeqBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
    @NotNull(message = "用例id不能为空")
    private Integer caseId;
    @NotNull(message = "序号不能为空")
    @Min(1)
    private Integer seq;
    @EnumValue(intValues = {0, 1, 2}, message = "类型只能为0-计划中用例,1-计划前用例,2-计划后用例")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
