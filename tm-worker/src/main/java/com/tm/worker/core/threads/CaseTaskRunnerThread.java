package com.tm.worker.core.threads;

import com.tm.common.base.model.PlanRunningConfigSnapshot;
import com.tm.common.entities.autotest.enumerate.PlanExecuteResultStatusEnum;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.common.enumerate.ResultCodeEnum;
import com.tm.common.utils.ResultUtils;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.task.CaseTask;
import com.tm.worker.core.task.PlanTask;
import com.tm.worker.core.task.TaskService;
import com.tm.worker.core.task.WorkerCaseTaskQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CaseTaskRunnerThread implements Runnable {
    public static final long DEFAULT_SLEEP_SECONDS = 100;
    private TaskService taskService;

    public CaseTaskRunnerThread(TaskService taskService) {
        this.taskService = taskService;
    }

    private int index = 0;

    @Override
    public void run() {
        log.info("worker runner");
        for (; ; ) {
            try {
                // 线程池没有空闲线程或者没有计划任务
                if (!taskService.canSubmitCaseTask() || taskService.isPlanTaskEmpty()) {
                    TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_SECONDS);
                    continue;
                }
                PlanTask planTask = taskService.getPlanTask(index);
                CaseTask caseTask = getCaseTask(planTask);
                if(caseTask != null) {
                    runCaseTask(planTask, caseTask);
                }
            } catch (InterruptedException e) {
                log.error("sleep error, ", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("worker error, ", e);
            }
        }
    }

    private CaseTask getCaseTask(PlanTask planTask) throws InterruptedException {
        if (planTask == null || planTask.isFinished()) {
            TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_SECONDS);
            index = 0;
            return null;
        }
        // 获取case task 队列
        WorkerCaseTaskQueue caseTaskQueue = taskService.getCaseTaskQueue(planTask.getPlanExecuteResult().getId());
        if (caseTaskQueue == null || caseTaskQueue.isEmpty()) {
            index++;
            return null;
        }
        // 获取运行时配置
        PlanRunningConfigSnapshot runningConfigSnapshot = planTask.getRunningConfigSnapshot();
        // 计划中最大同时可以执行用例数
        Integer maxOccurs = runningConfigSnapshot.getMaxOccurs();
        if (maxOccurs == null || maxOccurs < 1) {
            maxOccurs = 1;
        }
        // 计划中正在运行的用例数大于等于设置的最大并发数
        if (planTask.getVirtualRunningCount() >= maxOccurs) {
            TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_SECONDS);
            index++;
            return null;
        }
        CaseTask caseTask = null;
        if(maxOccurs == 1) {
            if(planTask.getFinishedCount() >= planTask.getPolledCount() && !planTask.isStopped()) {
                // 取case task 执行用例
                caseTask = caseTaskQueue.poll();
            }
        }else{
            caseTask = caseTaskQueue.poll();
        }

        if (caseTask == null) {
            TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_SECONDS);
            index++;
            return null;
        }
        return caseTask;
    }

    private void runCaseTask(PlanTask planTask, CaseTask caseTask) {
        log.info("取到一个用例任务，计划结果id： {}, 用例id：{}, 用例名称：{}", planTask.getPlanExecuteResultId(),
                caseTask.getAutoCase().getId(), caseTask.getAutoCase().getName());
        planTask.increasePolledCount();
        planTask.increaseVirtualRunningCount();

        runCase(planTask, caseTask).whenCompleteAsync((baseResponse, throwable) -> {
            if(baseResponse != null) {
                if(baseResponse.getCode().equals(ResultCodeEnum.PLAN_IS_STOPPED_NOT_RUN_CASE.getCode())) {
                    planTask.decreasePolledCount();
                } else {
                    teardownPlanTask(planTask, baseResponse);
                }
            }
            planTask.decreaseVirtualRunningCount();
        });
    }

    private void removePlanTask(PlanTask planTask) {
        boolean removed = taskService.removePlanTask(planTask.getPlanExecuteResultId());
        if (!removed) {
            log.error("remove failed, {}", planTask.getPlanExecuteResultId());
        }
    }

    private void teardownPlanTask(PlanTask planTask, BaseResponse baseResponse) {
        // 用例失败 并且 计划设置失败后停止执行
        if(baseResponse.getCode().equals(ResultCodeEnum.CASE_RUN_ERROR.getCode())
                && planTask.getRunningConfigSnapshot().getFailContinue().equals(0)) {
            log.info("用例失败，并且 计划设置失败后停止执行, {}", planTask.getPlanExecuteResultId());
            taskService.stopPlanTask(planTask);
        }
        planTask.increaseFinishedCount();
        if(baseResponse.getCode().equals(ResultCodeEnum.CASE_RUN_ERROR.getCode())) {
            planTask.increaseFailedCasesCount();
        }
        // 最后一个用例执行完，设置计划结果状态为完成
        if (planTask.isFinished()) {
            log.info("最后一个用例执行完，设置计划结果状态为完成, 计划结果id：{}", planTask.getPlanExecuteResultId());
            taskService.setPlanExecuteEnd(planTask);
            removePlanTask(planTask);
        } else if (planTask.getRunningTotal().equals(0) && planTask.isStopped() && !planTask.isCaseFailStopPlan()) {
            log.info("计划被用户停止，设置为停止状态, 计划结果id：{}", planTask.getPlanExecuteResultId());
            taskService.setPlanExecuteResultStatus(planTask, PlanExecuteResultStatusEnum.CANCELED);
            removePlanTask(planTask);
        }
        log.info("更新计划{}结果成功、失败数目", planTask.getPlanExecuteResultId());
        // 更新计划结果成功、失败数目
        if(baseResponse.getCode().equals(ResultCodeEnum.CASE_RUN_ERROR.getCode())) {
            taskService.setFailCount(planTask.getPlanExecuteResultId(), planTask.getFailedCasesCount());
        }
        taskService.setSuccessCount(planTask.getPlanExecuteResultId(),
                planTask.getFinishedCount() - planTask.getFailedCasesCount());
        planTask.decreaseRunningCount();
    }

    private CompletableFuture<BaseResponse> runCase(PlanTask planTask, CaseTask caseTask) {
        return CompletableFuture.supplyAsync(() -> {
            if (planTask.isStopped()) {
                return ResultUtils.error(ResultCodeEnum.PLAN_IS_STOPPED_NOT_RUN_CASE);
            }
            CaseTaskThread caseTaskThread = new CaseTaskThread(caseTask, taskService);
            try {
                return taskService.submitCaseTask(caseTaskThread).get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TMException(e);
            } catch (ExecutionException e) {
                throw new TMException(e);
            }
        });
    }
}
