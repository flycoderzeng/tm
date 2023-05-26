package com.tm.common.entities.mock.msg;

import lombok.Data;

@Data
public class MockAgentInstanceInfo {

    private String applicationName;
    private String ip;
    private Integer port;
    private String name;
    private String description;
}
