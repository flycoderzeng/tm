package com.tm.worker.core.task;

import com.tm.worker.core.threads.CaseTaskRunnerThread;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
@Order(1)
public class WorkerRunner implements ApplicationRunner {
    private final TaskService taskService;

    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public WorkerRunner(TaskService taskService) {
        this.taskService = taskService;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        threadPoolExecutor = (ThreadPoolExecutor) executorService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CaseTaskRunnerThread caseTaskRunnerThread = new CaseTaskRunnerThread(taskService);
        threadPoolExecutor.submit(caseTaskRunnerThread);
    }
}
