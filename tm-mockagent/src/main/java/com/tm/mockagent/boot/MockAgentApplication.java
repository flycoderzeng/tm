package com.tm.mockagent.boot;

import com.tm.mockagent.entities.model.MockAgentArgs;
import com.tm.mockagent.entities.model.MockAgentInstanceInfo;
import com.tm.mockagent.netty.MockNettyClient;
import com.tm.mockagent.rule.MockRuleFactory;
import com.tm.mockagent.threads.MockAgentThreadFactory;
import com.tm.mockagent.threads.MockNettyClientThread;
import com.tm.mockagent.threads.MockRuleManageThread;
import lombok.Data;
import org.apache.log4j.Logger;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class MockAgentApplication {
    private static final Logger logger = Logger.getLogger(MockAgentApplication.class);

    private MockAgentContext context;

    private ExecutorService executorService;

    private void initThreadPool() {
        MockAgentThreadFactory threadFactory = new MockAgentThreadFactory();
        executorService = Executors.newFixedThreadPool(2, threadFactory);
    }

    private void initContext(MockAgentArgs info) {
        context = new MockAgentContext();
        MockNettyClient nettyClient = new MockNettyClient(info.getMockServerIp(), info.getMockServerPort());
        MockRuleFactory ruleFactory = new MockRuleFactory();
        context.setNettyClient(nettyClient);
        context.setRuleFactory(ruleFactory);
        context.getMockMsgControlService().setMockAgentInstanceInfo(new MockAgentInstanceInfo(
                info.getApplicationName(),
                info.getIp(),
                info.getPort(), info.getName(), info.getDescription()));
    }

    public void run() {
        startMockNettyClient();
        startMockRuleManager();
    }

    public void startMockNettyClient() {
        logger.info("start mock netty client");
        executorService.submit(new MockNettyClientThread(context));
    }

    private void startMockRuleManager() {
        logger.info("request mock rules");
        executorService.submit(new MockRuleManageThread(context));
    }

    public void init(MockAgentArgs info) {
        initContext(info);
        initThreadPool();
    }

    public String getMockTargetUrl(String sourceUrl, String method) {
        return context.getRuleFactory().getMockTargetUrl(sourceUrl, method);
    }

    public URI getMockTargetUrl(URI sourceUrl, String method) {
        return context.getRuleFactory().getMockTargetUrl(sourceUrl, method);
    }
}
