package com.tm.mockagent.threads;

import com.tm.mockagent.boot.MockAgentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MockRuleManageThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MockRuleManageThread.class);

    public static final Integer MAX_LOOP_NUMBER = Integer.MAX_VALUE;
    public static final Integer DEFAULT_SLEEP_TIME = 10;

    private MockAgentContext context;

    public MockRuleManageThread(MockAgentContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < MAX_LOOP_NUMBER) {
            if (context.getMockMsgControlService().getAgentId() == null) {
                logger.info("注册mock agent");
                context.getMockMsgControlService().register(context.getNettyClient());
            } else {
                final Integer size = context.getRuleFactory().getHttpRulesSize();
                if (size < 1) {
                    logger.info("http mock规则是空, 请求加载mock规则");
                    context.getMockMsgControlService().requestMockRule(context.getNettyClient());
                }
            }
            sleep();
            i++;
        }
        logger.info("请求mock规则达到最大次数");
    }

    private void sleep() {
        logger.debug("休息一会儿");
        try {
            TimeUnit.SECONDS.sleep(DEFAULT_SLEEP_TIME);
        } catch (InterruptedException exception) {
            logger.error("sleep error, ", exception);
            Thread.currentThread().interrupt();
        }
    }
}
