package com.tm.common.entities.common.enumerate;

public enum LeftParenthesesEnum {
    LEFT_PARENTHESES(1, "左括号"), NONE(2, "无");
    private Integer value;

    private String description;

    private LeftParenthesesEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static LeftParenthesesEnum get(int value) {
        return switch (value) {
            case 1 -> LeftParenthesesEnum.LEFT_PARENTHESES;
            case 2 -> LeftParenthesesEnum.NONE;
            default -> null;
        };
    }
}
