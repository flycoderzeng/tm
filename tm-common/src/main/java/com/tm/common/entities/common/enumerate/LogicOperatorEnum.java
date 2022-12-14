package com.tm.common.entities.common.enumerate;

public enum LogicOperatorEnum {
    AND(1, "and"), OR(2, "or");
    private Integer value;

    private String description;

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
        switch (value) {
            case 1:
                return LogicOperatorEnum.AND;
            case 2:
                return LogicOperatorEnum.OR;
            default:
                return null;
        }
    }
}
