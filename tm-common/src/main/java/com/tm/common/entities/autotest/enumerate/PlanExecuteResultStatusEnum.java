package com.tm.common.entities.autotest.enumerate;


//计划结果状态 1-初始化中 2-任务初始化完毕，等待调度执行 3-执行中 4-执行完成 5-暂停中 6-取消执行 99-异常结束
public enum PlanExecuteResultStatusEnum {
    INIT(1),INIT_END(2),RUNNING(3),FINISHED(4),PAUSED(5),CANCELED(6),EXCEPTION(99);
    Integer value;
    PlanExecuteResultStatusEnum(int value) {this.value = value;}
    public Integer value() {return value;}
    public static PlanExecuteResultStatusEnum get(Integer value) {
        switch (value) {
            case 1:
                return PlanExecuteResultStatusEnum.INIT;
            case 2:
                return PlanExecuteResultStatusEnum.INIT_END;
            case 3:
                return PlanExecuteResultStatusEnum.RUNNING;
            case 4:
                return PlanExecuteResultStatusEnum.FINISHED;
            case 5:
                return PlanExecuteResultStatusEnum.PAUSED;
            case 6:
                return PlanExecuteResultStatusEnum.CANCELED;
            case 99:
                return PlanExecuteResultStatusEnum.EXCEPTION;
            default:
                return null;
        }
    }
}
