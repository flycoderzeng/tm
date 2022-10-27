package com.tm.common.entities.mock.msg;

import lombok.Data;

@Data
public class PushMockRuleMsg<T> extends MockRulePairMsg {
    private T mockRule;
}
