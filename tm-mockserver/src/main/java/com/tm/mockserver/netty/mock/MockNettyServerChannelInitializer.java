package com.tm.mockserver.netty.mock;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockNettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        log.info("新的客户端进入, IP: {}, Port: {}",socketChannel.localAddress().getAddress(),socketChannel.localAddress().getPort());
        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1, 4));
        socketChannel.pipeline().addLast("decoder", new MockMsgDecoder());
        socketChannel.pipeline().addLast("encoder", new MockMsgEncoder());
        socketChannel.pipeline().addLast(new MockNettyServerHandler());
    }
}