package com.tm.worker.core.task;

import com.tm.common.base.model.PlanExecuteResult;

import java.util.Comparator;

public class WorkerPlanTask {
    // 最高优先级排第一位
    public static final Comparator<WorkerPlanTask> comparator = (o1, o2) -> o2.priority - o1.priority;

    private Integer priority = 0;


    private volatile boolean running = true;

    private PlanExecuteResult planExecuteResult;

    public WorkerPlanTask(PlanExecuteResult planExecuteResult) {
        this.planExecuteResult = planExecuteResult;
    }

    public WorkerPlanTask(Integer priority) {
        this.priority = priority;
    }

    public WorkerPlanTask(Integer priority, PlanExecuteResult planExecuteResult) {
        this.priority = priority;
        this.planExecuteResult = planExecuteResult;
    }

    public PlanExecuteResult getPlanExecuteResult() {
        return planExecuteResult;
    }

    public Integer getPlanExecuteResultId() {
        return planExecuteResult.getId();
    }

    public void stop() {
        running = false;
    }

    public boolean isStopped() {
        return !running;
    }
}
