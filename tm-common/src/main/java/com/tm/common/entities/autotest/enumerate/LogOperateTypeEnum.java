package com.tm.common.entities.autotest.enumerate;

public enum LogOperateTypeEnum {
    INSERT(1, "insert"), UPDATE(2, "update");
    private Integer value;

    private String description;

    private LogOperateTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer val() {
        return value;
    }

    public String desc() {
        return description;
    }
}
