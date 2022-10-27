package com.tm.worker.core.task;

import com.tm.common.base.model.PlanExecuteResult;
import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.worker.core.variable.AutoTestVariables;

import java.util.concurrent.atomic.AtomicInteger;


public class PlanTask extends WorkerPlanTask {
    private PlanRunningConfigSnapshot runningConfigSnapshot;
    private AutoTestVariables globalVariables;

    private Integer totalCases = 0;

    private Boolean isUpdateRunning = false;

    private AtomicInteger finishedCasesCount = new AtomicInteger(0);

    private AtomicInteger runningCasesCount = new AtomicInteger(0);

    public PlanTask(Integer priority, PlanExecuteResult planExecuteResult, PlanRunningConfigSnapshot runningConfigSnapshot,
                    AutoTestVariables globalVariables) {
        super(priority, planExecuteResult);
        this.runningConfigSnapshot = runningConfigSnapshot;
        this.globalVariables = globalVariables;
    }

    public PlanTask(PlanExecuteResult planExecuteResult,
                    PlanRunningConfigSnapshot runningConfigSnapshot,
                    AutoTestVariables globalVariables) {
        super(planExecuteResult);
        this.runningConfigSnapshot = runningConfigSnapshot;
        this.globalVariables = globalVariables;
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

    public AutoTestVariables getGlobalVariables() {
        return globalVariables;
    }

    public Integer getRunningTotal() {
        return runningCasesCount.get();
    }

    public void setTotalCases(Integer total) {
        this.totalCases = total;
    }

    public Integer getTotalCases() {
        return totalCases;
    }
}
