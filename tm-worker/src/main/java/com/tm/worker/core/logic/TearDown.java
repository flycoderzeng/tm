package com.tm.worker.core.logic;


import com.tm.worker.core.node.StepNodeBase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class TearDown extends StepNodeBase {
    @Override
    public void run() {
        log.info("run {}", getName());
        log.info("run {} self end", getName());
    }
}
