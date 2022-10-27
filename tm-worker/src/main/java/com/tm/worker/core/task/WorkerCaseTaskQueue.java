package com.tm.worker.core.task;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class WorkerCaseTaskQueue {
    private ConcurrentLinkedQueue<CaseTask> caseTaskQueue = new ConcurrentLinkedQueue<>();
    private int total;

    public WorkerCaseTaskQueue() {
        total = 0;
    }
    public void add(CaseTask caseTask) {
        boolean add = caseTaskQueue.add(caseTask);
        if(!add) {
            log.error("添加case task到caseTaskQueue失败");
        }
        total++;
    }

    public int size() {
        return total;
    }

    public boolean isEmpty() {
        return caseTaskQueue.isEmpty();
    }

    public CaseTask poll() {
        return caseTaskQueue.poll();
    }
}
