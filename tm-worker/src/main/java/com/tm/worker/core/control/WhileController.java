package com.tm.worker.core.control;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhileController extends GenericController {
    @Override
    public void run() throws Exception {
        log.info("执行IF逻辑，{}，表达式：{}", getName(), condition);
        addResultInfo("表达式：").addResultInfo(condition).addResultInfo("\n");
        super.run();
    }
}
