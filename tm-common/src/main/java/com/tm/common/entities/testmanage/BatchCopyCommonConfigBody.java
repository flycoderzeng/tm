package com.tm.common.entities.testmanage;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BatchCopyCommonConfigBody {
    @NotNull(message = "源环境id不能为空")
    private Integer srcEnvId;

    private Integer srcDcnId;

    @NotNull(message = "目标环境id不能为空")
    private Integer desEnvId;

    @NotBlank(message = "IP地址不能为空")
    private String ip;

    @NotBlank(message = "端口号不能为空")
    private String port;
}
