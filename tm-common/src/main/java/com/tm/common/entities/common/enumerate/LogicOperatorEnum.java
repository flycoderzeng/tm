package com.tm.common.entities.common.enumerate;

public enum LogicOperatorEnum {
    AND(1, "and"), OR(2, "or");
    private final Integer value;

    private final String description;

    private LogicOperatorEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static LogicOperatorEnum get(int value) {
        return switch (value) {
            case 1 -> LogicOperatorEnum.AND;
            case 2 -> LogicOperatorEnum.OR;
            default -> null;
        };
    }
}
