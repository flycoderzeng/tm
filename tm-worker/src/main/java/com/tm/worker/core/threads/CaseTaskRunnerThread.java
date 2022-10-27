package com.tm.worker.core.threads;

import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.common.entities.base.BaseResponse;
import com.tm.worker.core.task.*;
import com.tm.worker.service.CaseResultLogService;
import com.tm.worker.service.DbConfigService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CaseTaskRunnerThread implements Runnable {
    public static final Integer DEFAULT_SLEEP_SECONDS = 1;
    private TaskService taskService;

    public CaseTaskRunnerThread(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run() {
        log.info("worker runner");
        for (; ; ) {
            try {
                // 线程池没有空闲线程或者没有计划任务
                if (!taskService.canSubmitCaseTask() || taskService.isPlanTaskEmpty()) {
                    TimeUnit.SECONDS.sleep(DEFAULT_SLEEP_SECONDS);
                    continue;
                }
                int index = 0;
                for (; ; ) {
                    PlanTask planTask = taskService.getPlanTask(index);
                    if (planTask == null || planTask.getFinishedCount().equals(planTask.getTotalCases())) {
                        TimeUnit.SECONDS.sleep(DEFAULT_SLEEP_SECONDS);
                        index = 0;
                        continue;
                    }
                    // 获取case task 队列
                    WorkerCaseTaskQueue caseTaskQueue = taskService.getCaseTaskQueue(planTask.getPlanExecuteResult().getId());
                    if (caseTaskQueue == null || caseTaskQueue.isEmpty()) {
                        continue;
                    }
                    // 获取运行时配置
                    PlanRunningConfigSnapshot runningConfigSnapshot = planTask.getRunningConfigSnapshot();
                    // 计划中当前运行的用例
                    Integer runningTotal = planTask.getRunningTotal();
                    // 计划中最大同时可以执行用例数
                    Integer maxOccurs = runningConfigSnapshot.getMaxOccurs();
                    if (maxOccurs == null || maxOccurs < 1) {
                        maxOccurs = 1;
                    }
                    // 计划中正在运行的用例数大于等于设置的最大并发数
                    if (runningTotal >= maxOccurs) {
                        TimeUnit.SECONDS.sleep(DEFAULT_SLEEP_SECONDS);
                        index++;
                        continue;
                    }
                    // 取case task 执行用例
                    CaseTask caseTask = caseTaskQueue.poll();
                    if (caseTask == null) {
                        TimeUnit.SECONDS.sleep(DEFAULT_SLEEP_SECONDS);
                        index++;
                        continue;
                    }
                    runCase(planTask, caseTask);
                }
            } catch (InterruptedException e) {
                log.error("sleep error, ", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("worker error, ", e);
            }
        }
    }

    private Future<BaseResponse> runCase(PlanTask planTask, CaseTask caseTask) {
        if (!planTask.isStopped()) {
            CaseTaskThread caseTaskThread = new CaseTaskThread(caseTask, taskService);
            return taskService.submitCaseTask(caseTaskThread);
        }
        return null;
    }
}
