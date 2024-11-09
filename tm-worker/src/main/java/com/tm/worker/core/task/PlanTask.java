package com.tm.worker.core.task;

import com.tm.common.base.model.PlanExecuteResult;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.worker.core.variable.AutoTestVariables;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;


public class PlanTask extends WorkerPlanTask {
    @Getter
    private PlanRunningConfigSnapshot runningConfigSnapshot;
    @Getter
    private AutoTestVariables planVariables;

    @Setter
    private Integer totalCases = -1;

    @Getter
    private Boolean isUpdateRunning = false;

    private final AtomicInteger finishedCasesCount = new AtomicInteger(0);

    private final AtomicInteger runningCasesCount = new AtomicInteger(0);

    private final AtomicInteger virtualRunningCasesCount = new AtomicInteger(0);

    private final AtomicInteger failedCasesCount = new AtomicInteger(0);

    private final AtomicInteger polledCount = new AtomicInteger(0);

    public PlanTask(Integer priority, PlanExecuteResult planExecuteResult, PlanRunningConfigSnapshot runningConfigSnapshot,
                    AutoTestVariables planVariables) {
        super(priority, planExecuteResult);
        this.runningConfigSnapshot = runningConfigSnapshot;
        this.planVariables = planVariables;
    }

    public PlanTask(PlanExecuteResult planExecuteResult,
                    PlanRunningConfigSnapshot runningConfigSnapshot,
                    AutoTestVariables planVariables) {
        super(planExecuteResult);
        this.runningConfigSnapshot = runningConfigSnapshot;
        this.planVariables = planVariables;
    }

    public void increaseRunningCount() {
        runningCasesCount.incrementAndGet();
    }

    public void increaseVirtualRunningCount() {
        virtualRunningCasesCount.incrementAndGet();
    }

    public Integer getVirtualRunningCount() {
        return virtualRunningCasesCount.get();
    }

    public void decreaseVirtualRunningCount() {
        virtualRunningCasesCount.decrementAndGet();
    }

    public void increaseFailedCasesCount() {
        failedCasesCount.incrementAndGet();
    }

    public Integer getFailedCasesCount() {
        return failedCasesCount.get();
    }

    public void setIsUpdateRunning() {
        isUpdateRunning = true;
    }

    public void decreaseRunningCount() {
        runningCasesCount.decrementAndGet();
    }

    public void increaseFinishedCount() {
        finishedCasesCount.incrementAndGet();
    }

    public Integer getFinishedCount() {
        return finishedCasesCount.get();
    }

    public Integer getRunningTotal() {
        return runningCasesCount.get();
    }

    public void increasePolledCount() {
        polledCount.incrementAndGet();
    }

    public void decreasePolledCount() {
        polledCount.decrementAndGet();
    }

    public Integer getPolledCount() {
        return polledCount.get();
    }

    synchronized public boolean isFinished() {
        return totalCases == finishedCasesCount.get();
    }
}
