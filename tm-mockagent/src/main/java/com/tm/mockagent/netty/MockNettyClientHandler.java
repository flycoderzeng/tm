package com.tm.mockagent.netty;


import com.tm.mockagent.UspMockAgent;
import com.tm.mockagent.entities.msg.MockCommunicationMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

public class MockNettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(MockNettyClientHandler.class);

    private final MockMsgControlService mockMsgControlService = UspMockAgent.application.getContext().getMockMsgControlService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端Active .....");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        logger.info("收到服务端报文: " + object);

        if (object instanceof MockCommunicationMsg<?> msg) {
            try {
                mockMsgControlService.process(ctx, msg);
            } catch (Exception e) {
                logger.error("process mock msg error, ", e);
            }
        } else {
            logger.error("invalid mock communication msg");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("error, ", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            logger.info("客户端循环心跳监测发送");
            mockMsgControlService.sendHeartbeatMsg(ctx);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
