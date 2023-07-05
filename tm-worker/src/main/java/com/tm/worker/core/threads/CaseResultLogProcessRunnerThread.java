package com.tm.worker.core.threads;

import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.worker.service.CaseResultLogService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CaseResultLogProcessRunnerThread implements Runnable {
    private CaseResultLogService caseResultLogService;
    private ConcurrentLinkedDeque<CaseExecuteLogOperate> queue;

    public CaseResultLogProcessRunnerThread(CaseResultLogService caseResultLogService) {
        this.caseResultLogService = caseResultLogService;
        this.queue = caseResultLogService.getLogQueue();
    }

    @Override
    public void run() {
        log.info("CaseResultLogProcessRunnerThread");
        for (; ; ) {
            CaseExecuteLogOperate logOperate = null;
            logOperate = queue.poll();
            if(logOperate == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    continue;
                } catch (InterruptedException e) {
                    log.error("sleep error, ", e);
                    Thread.interrupted();
                }
            }
            switch (logOperate.getLogOperateTypeEnum()) {
                case INSERT -> caseResultLogService.insert(logOperate.getLogRow());
                case UPDATE -> caseResultLogService.update(logOperate.getLogRow());
                default -> {
                }
            }
        }
    }
}
