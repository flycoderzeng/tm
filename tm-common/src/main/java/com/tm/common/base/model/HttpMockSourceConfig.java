package com.tm.common.base.model;


import lombok.Data;

@Data
public class HttpMockSourceConfig extends Common6ItemsModel {
    private String name;
    //1-http 2-https
    private Integer httpProtocolType;
    private String mockSourceIp;
    private Integer mockSourcePort;
}
