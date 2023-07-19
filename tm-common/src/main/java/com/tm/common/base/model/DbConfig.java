package com.tm.common.base.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DbConfig extends Common6ItemsModel {
    @NotBlank(message = "数据库名称不能为空")
    private String dbName;
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
}
