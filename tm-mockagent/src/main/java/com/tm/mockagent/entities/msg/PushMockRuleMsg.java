package com.tm.mockagent.entities.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PushMockRuleMsg<T> extends MockRulePairMsg {
    private T mockRule;
}
