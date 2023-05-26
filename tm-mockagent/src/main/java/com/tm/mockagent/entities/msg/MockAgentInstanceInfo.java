package com.tm.mockagent.entities.msg;

import lombok.Data;

@Data
public class MockAgentInstanceInfo {
    private String applicationName;
    private String ip;
    private Integer port;
    private String name;
    private String description;

    public MockAgentInstanceInfo() {}

    public MockAgentInstanceInfo(String applicationName,
                                 String ip, Integer port, String name, String description) {
        this.applicationName = applicationName;
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.description = description;
    }
}
