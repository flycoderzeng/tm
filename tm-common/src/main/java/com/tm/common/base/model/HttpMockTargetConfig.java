package com.tm.common.base.model;


import lombok.Data;

@Data
public class HttpMockTargetConfig extends Common6ItemsModel {
    private String name;
    private String mockTargetIp;
    private Integer mockTargetPort;
}
