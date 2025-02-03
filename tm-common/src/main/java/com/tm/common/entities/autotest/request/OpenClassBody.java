package com.tm.common.entities.autotest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OpenClassBody {
    @NotNull(message = "id不能为空")
    private Integer id;
    @NotBlank(message = "packageName不能为空")
    private String packageName;
    @NotBlank(message = "sourceFileName不能为空")
    private String sourceFileName;
}
