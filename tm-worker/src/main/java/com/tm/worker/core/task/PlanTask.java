package com.tm.worker.core.task;

import com.tm.common.base.model.PlanExecuteResult;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.worker.core.variable.AutoTestVariables;

import java.util.concurrent.atomic.AtomicInteger;


public class PlanTask extends WorkerPlanTask {
    private PlanRunningConfigSnapshot runningConfigSnapshot;
    private AutoTestVariables planVariables;

    private Integer totalCases = -1;

    private Boolean isUpdateRunning = false;

    private AtomicInteger finishedCasesCount = new AtomicInteger(0);

    private AtomicInteger runningCasesCount = new AtomicInteger(0);

    private AtomicInteger polledCount = new AtomicInteger(0);

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

    public Boolean getIsUpdateRunning() {
        return isUpdateRunning;
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

    public PlanRunningConfigSnapshot getRunningConfigSnapshot() {
        return runningConfigSnapshot;
    }

    public AutoTestVariables getPlanVariables() {
        return planVariables;
    }

    public Integer getRunningTotal() {
        return runningCasesCount.get();
    }

    public void increasePolledCount() {
        polledCount.incrementAndGet();
    }

    public Integer getPolledCount() {
        return polledCount.get();
    }

    public void setTotalCases(Integer total) {
        this.totalCases = total;
    }

    synchronized public boolean isFinished() {
        return totalCases == finishedCasesCount.get();
    }
}
