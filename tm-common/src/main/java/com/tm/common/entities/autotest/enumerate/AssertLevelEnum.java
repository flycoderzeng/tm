package com.tm.common.entities.autotest.enumerate;

public enum AssertLevelEnum {
    ERROR("error", "断言失败，用例结束"), WARNING("warning", "断言失败，用例继续执行");
    String value;
    String description;
    AssertLevelEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String val() {
        return value;
    }

    public String desc() {
        return description;
    }

    public static AssertLevelEnum get(String value) {
        return switch (value) {
            case "error" -> AssertLevelEnum.ERROR;
            case "warning" -> AssertLevelEnum.WARNING;
            default -> null;
        };
    }
}
