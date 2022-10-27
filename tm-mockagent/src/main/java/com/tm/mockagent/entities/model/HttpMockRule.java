package com.tm.mockagent.entities.model;

import lombok.Data;

@Data
public class HttpMockRule {
    private Integer id;
    private String name;
    private String uri;
    //1-GET 2-POST
    private Integer method;
    private String responseRule;
    // 0-启用 1-禁用
    private Integer enabled;

    private String mockSourceIp;
    private Integer mockSourcePort;
    //1-http 2-https
    private Integer httpProtocolType;
    private String mockTargetIp;
    private Integer mockTargetPort;
}
