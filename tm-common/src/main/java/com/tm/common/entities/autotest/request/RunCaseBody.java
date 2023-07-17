package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RunCaseBody {
    @NotNull(message = "用例id不能为空")
    private Integer caseId;
    @NotNull(message = "运行类型不能为空")
    @Min(1)
    @Max(2)
    private Integer runType;
    private Integer runEnvId;
    private String runDescription;
}
