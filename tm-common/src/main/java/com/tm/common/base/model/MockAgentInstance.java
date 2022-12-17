package com.tm.common.base.model;

import lombok.Data;

import java.util.Date;


@Data
public class MockAgentInstance {

    private String applicationName;
    private Integer id;
    private String ip;
    private Integer port;
    private String name;
    private String description;
    private Date firstRegisterTime;
    private Date lastRegisterTime;
    private Integer status;
    private Integer total;
    private Integer onlineTotal;
}
