package com.tm.common.base.model;

import lombok.Data;

@Data
public class RunEnv extends Common6ItemsModel {
    private String name;
    private String description;
    private String httpIp;
    private String httpPort;
    private String dbUsername;
    private String dbPassword;
    private String dbIp;
    private String dbPort;
    private Integer dbType;
    private String dbSchemaName;
}
