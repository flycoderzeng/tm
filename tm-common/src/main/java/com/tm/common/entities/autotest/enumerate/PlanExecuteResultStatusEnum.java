package com.tm.common.entities.autotest.enumerate;


// 计划结果状态 1-初始化中 2-任务初始化完毕，等待调度执行 3-执行中 4-执行完成 5-暂停中 6-停止执行 7-任务队列已满
// 8-setup计划执行结果存在失败用例 9-setup计划执行中 10-有用例执行失败,停止执行
// 99-异常结束
public enum PlanExecuteResultStatusEnum {
    INIT(1),INIT_END(2),RUNNING(3),FINISHED(4),
    PAUSED(5),CANCELED(6),TASK_OVERFLOW(7), SETUP_FAIL(8),
    SETUP_PLAN_RUNNING(9), CASE_FAIL_STOP_PLAN(10),
    EXCEPTION(99);
    Integer value;
    PlanExecuteResultStatusEnum(int value) {this.value = value;}
    public Integer value() {return value;}
    public static PlanExecuteResultStatusEnum get(Integer value) {
        return switch (value) {
            case 1 -> PlanExecuteResultStatusEnum.INIT;
            case 2 -> PlanExecuteResultStatusEnum.INIT_END;
            case 3 -> PlanExecuteResultStatusEnum.RUNNING;
            case 4 -> PlanExecuteResultStatusEnum.FINISHED;
            case 5 -> PlanExecuteResultStatusEnum.PAUSED;
            case 6 -> PlanExecuteResultStatusEnum.CANCELED;
            case 7 -> PlanExecuteResultStatusEnum.TASK_OVERFLOW;
            case 8 -> PlanExecuteResultStatusEnum.SETUP_FAIL;
            case 9 -> PlanExecuteResultStatusEnum.SETUP_PLAN_RUNNING;
            case 10 -> PlanExecuteResultStatusEnum.CASE_FAIL_STOP_PLAN;
            case 99 -> PlanExecuteResultStatusEnum.EXCEPTION;
            default -> null;
        };
    }
}
