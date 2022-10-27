package com.tm.mockagent;

import com.tm.mockagent.entities.msg.BaseMockMsg;
import com.tm.mockagent.entities.msg.MockCommunicationMsg;
import com.tm.mockagent.entities.enumerate.MockMsgType;
import com.tm.mockagent.netty.MockNettyClient;

import java.util.concurrent.TimeUnit;

public class MockAgentTest {
    public static void main(String[] args) throws InterruptedException {
        MockNettyClient mockNettyClient = new MockNettyClient("127.0.0.1", 54321);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mockNettyClient.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("start netty client success");
        mockNettyClient.send(new MockCommunicationMsg<BaseMockMsg>((byte)MockMsgType.HEARTBEAT.val(),
                new BaseMockMsg(1, "")));
    }
}
