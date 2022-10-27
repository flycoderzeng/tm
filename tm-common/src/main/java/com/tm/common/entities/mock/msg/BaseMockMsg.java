package com.tm.common.entities.mock.msg;

import lombok.Data;

@Data
public class BaseMockMsg {
    private Integer agentId;
    private String actionTime;

    public BaseMockMsg() {}

    public BaseMockMsg(Integer agentId, String actionTime) {
        this.agentId = agentId;
        this.actionTime = actionTime;
    }
}
