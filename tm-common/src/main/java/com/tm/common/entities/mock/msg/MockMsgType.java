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
        return switch (value) {
            case 0 -> HEARTBEAT;
            case 1 -> HEARTBEAT_ACK;
            case 2 -> REGISTER;
            case 3 -> REGISTER_ACK;
            case 4 -> REQUEST_LOAD_MOCK_RULE;
            case 5 -> REQUEST_LOAD_MOCK_RULE_ACK;
            case 6 -> PUSH_MOCK_RULE;
            case 7 -> PUSH_MOCK_RULE_ACK;
            case 8 -> ENABLE_MOCK_RULE;
            case 9 -> ENABLE_MOCK_RULE_ACK;
            case 10 -> DISABLE_MOCK_RULE;
            case 11 -> DISABLE_MOCK_RULE_ACK;
            case 12 -> REMOVE_MOCK_RULE;
            case 13 -> REMOVE_MOCK_RULE_ACK;
            default -> null;
        };
    }
}
