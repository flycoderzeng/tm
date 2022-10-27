package com.tm.mockagent.entities.msg;

import lombok.Data;

@Data
public class PushMockRuleMsg<T> extends MockRulePairMsg {
    private T mockRule;
}
