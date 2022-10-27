package com.tm.common.entities.testmanage;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckExpressionBody {
    @NotBlank(message = "表达式不能为空")
    private String expression;
}
