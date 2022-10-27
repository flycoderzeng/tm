package com.tm.mockserver.netty.mock;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChannelHandlerContextFactory {
    private Map<Integer, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();

    public void registerAgent(Integer mockAgentId, ChannelHandlerContext ctx) {
        ctxMap.put(mockAgentId, ctx);
    }

    public ChannelHandlerContext getChannelHandlerContext(Integer mockAgentId) {
        return ctxMap.get(mockAgentId);
    }
}
