package com.tm.common.entities.mock.msg;

public enum MockMsgType {
    HEARTBEAT (0),
    HEARTBEAT_ACK (1),
    REGISTER (2),
    REGISTER_ACK (3),
    REQUEST_LOAD_MOCK_RULE (4),
    REQUEST_LOAD_MOCK_RULE_ACK (5),
    PUSH_MOCK_RULE (6),
    PUSH_MOCK_RULE_ACK (7),
    ENABLE_MOCK_RULE (8),
    ENABLE_MOCK_RULE_ACK (9),
    DISABLE_MOCK_RULE (10),
    DISABLE_MOCK_RULE_ACK (11),
    REMOVE_MOCK_RULE (12),
    REMOVE_MOCK_RULE_ACK (13);
    private final int value;
    MockMsgType(int value) {this.value = value;}

    public int val() {
        return this.value;
    }

    public static MockMsgType get(int value) {
        switch (value) {
            case 0:
                return HEARTBEAT;
            case 1:
                return HEARTBEAT_ACK;
            case 2:
                return REGISTER;
            case 3:
                return REGISTER_ACK;
            case 4:
                return REQUEST_LOAD_MOCK_RULE;
            case 5:
                return REQUEST_LOAD_MOCK_RULE_ACK;
            case 6:
                return PUSH_MOCK_RULE;
            case 7:
                return PUSH_MOCK_RULE_ACK;
            case 8:
                return ENABLE_MOCK_RULE;
            case 9:
                return ENABLE_MOCK_RULE_ACK;
            case 10:
                return DISABLE_MOCK_RULE;
            case 11:
                return DISABLE_MOCK_RULE_ACK;
            case 12:
                return REMOVE_MOCK_RULE;
            case 13:
                return REMOVE_MOCK_RULE_ACK;
            default:
                return null;
        }
    }
}
