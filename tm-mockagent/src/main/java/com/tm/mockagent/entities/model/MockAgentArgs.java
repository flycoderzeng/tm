package com.tm.mockagent.entities.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MockAgentArgs extends MockAgentInstanceInfo {
    private String mockServerIp;
    private Integer mockServerPort;
}
