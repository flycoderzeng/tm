package com.tm.common.base.model;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AutoScript extends CommonNameDescriptionModel {
    public static final Integer AutoScriptTypeShell = 1;

    @NotNull(message = "id不能为空")
    private Integer id;
    @NotNull(message = "type不能为空")
    private Integer type;
    @NotNull(message = "content不能为空")
    private String content;
}
