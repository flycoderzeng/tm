package com.tm.mockagent.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MockAgentThreadFactory implements ThreadFactory {
    private AtomicInteger seq = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        int c = seq.incrementAndGet();
        return new Thread(r, "mock-agent-thread-" + c);
    }
}
