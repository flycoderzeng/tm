package com.tm.mockagent.netty;

import com.tm.mockagent.entities.msg.MockCommunicationMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

@Data
public class MockNettyClient {
    private static final Logger logger = Logger.getLogger(MockNettyClient.class);

    private ChannelFuture future;
    private String ip;
    private Integer port;
    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public MockNettyClient(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    private void initNetty() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap()
            .group(group)
            //该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .channel(NioSocketChannel.class)
            .handler(new MockNettyClientInitializer());
    }

    public void start() throws InterruptedException {
        initNetty();
        future = bootstrap.connect(ip, port).sync();
        logger.info("连接服务端成功....");
    }

    public void send(MockCommunicationMsg msg) {
        if(msg == null) {
            return;
        }

        while (future == null) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                logger.error("sleep error, ", e);
                Thread.currentThread().interrupt();
            }
        }
        future.channel().writeAndFlush(msg);
        logger.info("发送消息成功");
    }

    public void shutdown() {
        group.shutdownGracefully();
        future.channel().close();
        future.channel().disconnect();
        future.channel().deregister();
        future = null;
    }
}