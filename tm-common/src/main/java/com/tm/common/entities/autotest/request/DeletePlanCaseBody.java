package com.tm.common.entities.autotest.request;

import com.tm.common.entities.common.CommonIdListBody;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeletePlanCaseBody extends CommonIdListBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
}
