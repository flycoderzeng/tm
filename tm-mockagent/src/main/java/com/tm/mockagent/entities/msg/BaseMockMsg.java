package com.tm.mockagent.entities.msg;

import lombok.Data;

@Data
public class BaseMockMsg {
    private Integer agentId;
    private String actionTime;

    public BaseMockMsg(Integer agentId, String actionTime) {
        this.agentId = agentId;
        this.actionTime = actionTime;
    }
}
