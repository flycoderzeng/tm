package com.tm.common.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class DcnConfig extends Common6ItemsModel {
    @NotBlank(message = "DCN名称不能为空")
    private String dcnName;

    private String dcnDescription;
}
