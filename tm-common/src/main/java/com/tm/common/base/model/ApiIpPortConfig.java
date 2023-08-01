package com.tm.common.base.model;

import lombok.Data;

@Data
public class ApiIpPortConfig extends Common6ItemsModel {
    private String name;
    private String url;
    private Integer envId;
    private String envName;
    private String ip;
    private String port;
    private Integer dcnId;
}
