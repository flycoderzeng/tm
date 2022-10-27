package com.tm.mockagent.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MockNettyClientInitializer extends ChannelInitializer<SocketChannel> {
    public static final Integer READER_IDLE_TIME = 10;
    public static final Integer WRITER_IDLE_TIME = 10;
    public static final Integer ALL_IDLE_TIME = 0;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(new IdleStateHandler(READER_IDLE_TIME,WRITER_IDLE_TIME,ALL_IDLE_TIME, TimeUnit.SECONDS));
        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1, 4));
        socketChannel.pipeline().addLast("encoder", new MockMsgEncoder());
        socketChannel.pipeline().addLast("decoder", new MockMsgDecoder());
        socketChannel.pipeline().addLast(new MockNettyClientHandler());
    }
}
