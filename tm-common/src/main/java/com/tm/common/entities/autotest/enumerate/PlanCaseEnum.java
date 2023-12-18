package com.tm.common.entities.autotest.enumerate;

//0-计划用例 1-计划setup用例 2-计划teardown用例
public enum PlanCaseEnum {
    DEFAULT(0), SETUP(1), TEARDOWN(2);
    int value;
    PlanCaseEnum(int value) {this.value = value;}
    PlanCaseEnum() {}
    public Integer value() {return value;}
    public static PlanCaseEnum get(int value) {
        return switch (value) {
            case 0 -> PlanCaseEnum.DEFAULT;
            case 1 -> PlanCaseEnum.SETUP;
            case 2 -> PlanCaseEnum.TEARDOWN;
            default -> null;
        };
    }
}
