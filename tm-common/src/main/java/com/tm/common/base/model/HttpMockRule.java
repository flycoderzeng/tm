package com.tm.common.base.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HttpMockRule extends Common6ItemsModel {
    @NotBlank(message = "名称不能为空")
    private String name;
    private String description;
    @NotBlank(message = "uri不能为空")
    private String uri;
    //1-GET 2-POST
    @NotNull(message = "method不能为空")
    private Integer method;
    @NotBlank(message = "响应配置不能为空")
    private String responseRule;
    // 0-启用 1-禁用
    private Integer enabled = 0;

    private String mockSourceIp;
    private Integer mockSourcePort;
    //1-http 2-https
    private Integer httpProtocolType;
    private String mockTargetIp;
    private Integer mockTargetPort;
}
