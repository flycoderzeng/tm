package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OpenPackageBody {
    @NotNull(message = "id不能为空")
    private Integer id;
    @NotBlank(message = "packageName不能为空")
    private String packageName;
}
