package com.tm.common.entities.common;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommonIdBody {
    @NotNull(message = "id不能为空")
    private Integer id;
}
