package com.tm.common.entities.autotest.enumerate;

//1-非组合方式运行 2-组合方式运行
public enum PlanRunTypeEnum {
    DEFAULT(1),GROUP(2);
    int value;
    PlanRunTypeEnum(int value) {this.value = value;}
    PlanRunTypeEnum() {}
    public Integer value() {return value;}
    public static PlanRunTypeEnum get(int value) {
        return switch (value) {
            case 1 -> PlanRunTypeEnum.DEFAULT;
            case 2 -> PlanRunTypeEnum.GROUP;
            default -> null;
        };
    }
}
