package com.tm.common.base.model;

import lombok.Data;

@Data
public class MockRuleAgentRelation extends Common6ItemsModel {
    private Integer mockRuleId;
    private Integer mockRuleType = 1;
    private Integer mockAgentId;
    private Integer enabled;
    private Integer mockSourceConfigId;
    private Integer mockTargetConfigId;
}
