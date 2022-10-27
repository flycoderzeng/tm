package com.tm.mockagent.entities.model;

import com.tm.mockagent.entities.msg.MockAgentInstanceInfo;
import lombok.Data;

@Data
public class MockAgentArgsInfo extends MockAgentInstanceInfo {
    private String mockServerIp;
    private Integer mockServerPort;
}
