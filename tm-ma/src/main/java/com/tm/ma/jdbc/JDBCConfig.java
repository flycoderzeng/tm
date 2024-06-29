package com.tm.ma.jdbc;

import lombok.Data;

import java.io.Serializable;


@Data
public class JDBCConfig implements Serializable {
    private String jdbcUrl;
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    private String username;
    private String password;
    private int maxPoolSize = 300;
    private int minPoolSize = 3;
    private String poolType = "druid";
}