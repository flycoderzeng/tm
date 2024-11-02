package com.tm.worker.message;

import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.worker.service.CaseResultLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RabbitListener(queues = TestResultLogDirectRabbitConfig.TEST_RESULT_LOG_DIRECT_QUEUE, concurrency = "5")
public class TestResultLogDirectProcess {

    @Autowired
    private CaseResultLogService caseResultLogService;

    @RabbitHandler
    public void process(CaseExecuteLogOperate logOperate) {
        switch (logOperate.getLogOperateTypeEnum()) {
            case INSERT -> caseResultLogService.insert(logOperate.getLogRow());
            case UPDATE -> {
                int update = caseResultLogService.update(logOperate.getLogRow());
                if (update == 0) {
                    log.info("update 0 row");
                    caseResultLogService.put(logOperate);
                }
            }
            default -> {
            }
        }
    }
}