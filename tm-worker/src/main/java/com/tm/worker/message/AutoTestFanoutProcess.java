package com.tm.worker.message;

import com.tm.common.entities.autotest.enumerate.RabbitMessageTypeEnum;
import com.tm.common.entities.autotest.message.AutoTestRabbitMessage;
import com.tm.common.entities.autotest.request.StopPlanBody;
import com.tm.worker.core.task.TaskService;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(),
        exchange = @Exchange(value = AutoTestFanoutRabbitConfig.AUTO_TEST_FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT)
    )
)
public class AutoTestFanoutProcess {
    private final TaskService taskService;

    @Inject
    public AutoTestFanoutProcess(TaskService taskService) {
        this.taskService = taskService;
    }

    @RabbitHandler
    public void process(AutoTestRabbitMessage message) {
        final RabbitMessageTypeEnum messageTypeEnum = message.getRabbitMessageTypeEnum();
        switch (messageTypeEnum) {
            case STOP_PLAN:
                StopPlanBody body = (StopPlanBody) message.getObject();
                log.info("user {} 停止计划,结果id {}", body.getUser().getUsername(), body.getPlanResultId());
                taskService.stopPlanTask(body.getPlanResultId());
                break;
            default:
                break;
        }
    }
}
