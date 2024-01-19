package com.tm.common.base.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DbConfig extends Common6ItemsModel {
    @NotBlank(message = "数据库名称不能为空")
    private String dbName;
    private String schemaName;
    private String username;
    private String password;
    private String ip;
    private String port;
    private Integer envId;
    private String envName;
    private Integer dcnId;
    private Integer type;

    public String getDataSourceKey() {
        return this.dbName + "-" + this.envId;
    }

    public DbConfig() {

    }

    public DbConfig(String username, String password, String ip, String port, Integer type) {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;
        this.type = type;
    }
}
