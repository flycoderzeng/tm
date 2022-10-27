package com.tm.mockagent.boot;

import com.tm.mockagent.netty.MockMsgControlService;
import com.tm.mockagent.netty.MockNettyClient;
import com.tm.mockagent.rule.MockRuleFactory;
import lombok.Data;

@Data
public class MockAgentContext {
    private MockNettyClient nettyClient;
    private MockRuleFactory ruleFactory;
    private MockMsgControlService mockMsgControlService = new MockMsgControlService();

}
