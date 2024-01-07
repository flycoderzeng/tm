package com.tm.common.entities.autotest.enumerate;

public enum RabbitMessageTypeEnum {
    STOP_PLAN(1);
    int value;
    RabbitMessageTypeEnum(int value) {this.value = value;}
    RabbitMessageTypeEnum() {}
    public Integer value() {return value;}
    public static RabbitMessageTypeEnum get(int value) {
        return switch (value) {
            case 1 -> RabbitMessageTypeEnum.STOP_PLAN;
            default -> null;
        };
    }
}
