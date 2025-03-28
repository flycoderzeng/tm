package com.tm.common.entities.common.enumerate;

public enum RightParenthesesEnum {
    RIGHT_PARENTHESES(1, "右括号"), NONE(2, "无");
    private final Integer value;

    private final String description;

    private RightParenthesesEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static RightParenthesesEnum get(int value) {
        return switch (value) {
            case 1 -> RightParenthesesEnum.RIGHT_PARENTHESES;
            case 2 -> RightParenthesesEnum.NONE;
            default -> null;
        };
    }
}
