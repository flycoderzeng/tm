package com.tm.common.entities.autotest.message;

import com.tm.common.entities.autotest.enumerate.RabbitMessageTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class AutoTestRabbitMessage implements Serializable {
    private RabbitMessageTypeEnum rabbitMessageTypeEnum;
    private Object object;

    public AutoTestRabbitMessage(RabbitMessageTypeEnum rabbitMessageTypeEnum, Object object) {
        this.rabbitMessageTypeEnum = rabbitMessageTypeEnum;
        this.object = object;
    }
}
