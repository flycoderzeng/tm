package com.tm.common.base.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DcnConfig extends Common6ItemsModel {
    @NotBlank(message = "DCN名称不能为空")
    private String dcnName;

    private String dcnDescription;
}