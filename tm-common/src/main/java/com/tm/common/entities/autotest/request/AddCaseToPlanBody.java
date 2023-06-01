package com.tm.common.entities.autotest.request;

import com.tm.common.retention.EnumValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddCaseToPlanBody {
    @NotNull(message = "计划id不能为空")
    private Integer planId;
    @NotNull(message = "用例id列表不能为空")
    private List<Integer> caseIdList;
    @EnumValue(intValues = {0, 1, 2}, message = "类型只能为0-计划中用例,1-计划前用例,2-计划后用例")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
