package com.tm.mockagent.threads;

import com.tm.mockagent.boot.MockAgentContext;
import com.tm.mockagent.netty.MockNettyClient;
import com.tm.mockagent.rule.MockRuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MockNettyClientThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MockNettyClientThread.class);

    public static final Integer MAX_LOOP_NUMBER = Integer.MAX_VALUE;
    public static final Integer DEFAULT_SLEEP_TIME = 10;

    private MockAgentContext context;

    public MockNettyClientThread(MockAgentContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        loopStart();
    }

    private void loopStart() {
        MockNettyClient nettyClient = context.getNettyClient();
        MockRuleFactory ruleFactory = context.getRuleFactory();
        int i = 0;
        while (i < MAX_LOOP_NUMBER) {
            try {
                if(context.getNettyClient().getFuture() == null) {
                    nettyClient.start();
                }
                if(context.getMockMsgControlService().isHeartbeatCheckTimeout()) {
                    logger.info("已经长时间没有收到来自服务端的心跳ack消息了，重连服务端");
                    nettyClient.shutdown();
                    context.getMockMsgControlService().setLastReceiveHeartbeatAckTimestamp(System.currentTimeMillis());
                }
            } catch (Exception e) {
                try {
                    nettyClient.shutdown();
                } catch (Exception err) {
                    logger.error("shutdown netty client error, ", err);
                }
                logger.error("netty start error, ", e);
                logger.info("清空mock规则");
                ruleFactory.clear();
                logger.info("休眠一会儿");
                i++;
            }
            try {
                TimeUnit.SECONDS.sleep(DEFAULT_SLEEP_TIME);
            } catch (InterruptedException exception) {
                logger.error("sleep error, ", exception);
                Thread.currentThread().interrupt();
            }
        }
    }
}
