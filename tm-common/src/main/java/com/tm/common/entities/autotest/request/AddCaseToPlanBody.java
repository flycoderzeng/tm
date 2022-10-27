package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddCaseToPlanBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
    @NotNull(message = "用例id列表不能为空")
    private List<Integer> caseIdList;
}
