package com.tm.worker.message;

import com.tm.common.entities.autotest.CaseExecuteLogOperate;
import com.tm.common.entities.autotest.enumerate.RabbitMessageTypeEnum;
import com.tm.common.entities.autotest.message.AutoTestRabbitMessage;
import com.tm.common.entities.autotest.request.StopPlanBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageSendReceiveService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTestResultLog(CaseExecuteLogOperate logOperate) {
        rabbitTemplate.convertAndSend(TestResultLogDirectRabbitConfig.TEST_RESULT_LOG_DIRECT_EXCHANGE,
                TestResultLogDirectRabbitConfig.TEST_RESULT_LOG_DIRECT_ROUTING, logOperate);
    }

    public void stopPlan(StopPlanBody body) {
        rabbitTemplate.convertAndSend(AutoTestFanoutRabbitConfig.AUTO_TEST_FANOUT_EXCHANGE,
                null,
                new AutoTestRabbitMessage(RabbitMessageTypeEnum.STOP_PLAN, body));
    }
}