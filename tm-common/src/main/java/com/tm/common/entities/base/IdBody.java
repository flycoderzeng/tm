package com.tm.common.entities.base;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdBody {
    @NotNull(message = "id不能为空")
    private Integer id;
}
