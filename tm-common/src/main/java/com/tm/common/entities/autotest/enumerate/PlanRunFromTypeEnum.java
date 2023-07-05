package com.tm.common.entities.autotest.enumerate;

//1-计划2-用例3-定时计划
public enum PlanRunFromTypeEnum {
    PLAN(1),CASE(2),CRON_JOB(3);
    int value;
    PlanRunFromTypeEnum(int value) {this.value = value;}
    PlanRunFromTypeEnum() {}
    public Integer value() {return value;}
    public static PlanRunFromTypeEnum get(int value) {
        return switch (value) {
            case 2 -> PlanRunFromTypeEnum.CASE;
            case 3 -> PlanRunFromTypeEnum.CRON_JOB;
            case 1 -> PlanRunFromTypeEnum.PLAN;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
