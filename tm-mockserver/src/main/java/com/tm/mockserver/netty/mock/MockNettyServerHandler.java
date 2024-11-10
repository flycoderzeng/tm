package com.tm.mockserver.netty.mock;

import com.tm.common.entities.mock.msg.MockCommunicationMsg;
import com.tm.mockserver.service.MockMsgControlService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Slf4j
@Component
@ChannelHandler.Sharable
public class MockNettyServerHandler extends ChannelInboundHandlerAdapter {

    private static MockNettyServerHandler serverHandler;

    private final MockMsgControlService mockMsgControlService;

    public MockNettyServerHandler(MockMsgControlService mockMsgControlService) {
        this.mockMsgControlService = mockMsgControlService;
    }

    @PostConstruct
    public void init(){
        serverHandler = this;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel active......， {}",
                ((InetSocketAddress)ctx.channel().
                        remoteAddress()).getAddress().getHostAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        log.debug("收到客户端报文: {} ", object);
        if (object instanceof MockCommunicationMsg) {
            MockCommunicationMsg<Object> msg = (MockCommunicationMsg) object;
            try {
                serverHandler.mockMsgControlService.process(msg, ctx);
            } catch (Exception e) {
                log.error("process mock msg error, ", e);
            }
        } else {
            log.error("invalid mock communication msg");
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        log.info("客户端被移除，channelId为：{}", channelId);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("error, ", cause);
        ctx.close();
    }
}
