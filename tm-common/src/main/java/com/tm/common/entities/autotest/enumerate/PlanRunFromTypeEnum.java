package com.tm.common.entities.autotest.enumerate;

//1-计划2-用例3-定时计划
public enum PlanRunFromTypeEnum {
    PLAN(1),CASE(2),CRON_JOB(3);
    int value;
    PlanRunFromTypeEnum(int value) {this.value = value;}
    PlanRunFromTypeEnum() {}
    public Integer value() {return value;}
    public static PlanRunFromTypeEnum get(int value) {
        switch (value) {
            case 1:
                return PlanRunFromTypeEnum.PLAN;
            case 2:
                return PlanRunFromTypeEnum.CASE;
            case 3:
                return PlanRunFromTypeEnum.CRON_JOB;
            default:
                return null;
        }
    }
}
