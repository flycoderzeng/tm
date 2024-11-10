package com.tm.mockagent.netty;

import com.tm.mockagent.UspMockAgent;
import com.tm.mockagent.entities.enumerate.MockMsgType;
import com.tm.mockagent.entities.model.MockAgentInstanceInfo;
import com.tm.mockagent.entities.msg.BaseMockMsg;
import com.tm.mockagent.entities.msg.MockCommunicationMsg;
import com.tm.mockagent.entities.msg.PushMockRuleMsg;
import com.tm.mockagent.utils.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import org.apache.log4j.Logger;

import java.util.Objects;

@Data
public class MockMsgControlService {
    private static final Logger logger = Logger.getLogger(MockMsgControlService.class);
    private volatile Integer agentId = null;
    private MockAgentInstanceInfo mockAgentInstanceInfo;
    private long lastReceiveHeartbeatAckTimestamp = System.currentTimeMillis();

    public void process(ChannelHandlerContext ctx, MockCommunicationMsg<?> communicationMsg) {
        final MockMsgType mockMsgType = MockMsgType.get(communicationMsg.getType());
        Objects.requireNonNull(mockMsgType, "invalid mock msg type");

        switch (mockMsgType) {
            case HEARTBEAT_ACK:
                logger.debug("收到来自服务端的心跳ack消息, " + communicationMsg);
                processHeartbeatAckMsg();
                break;
            case REGISTER_ACK:
                logger.debug("收到来自服务端的注册ack消息, " + communicationMsg);
                processRegisterAckMsg(communicationMsg);
                break;
            case REQUEST_LOAD_MOCK_RULE_ACK:
                logger.debug("收到来自服务端的加载mock规则ack消息， " + communicationMsg);
                break;
            case PUSH_MOCK_RULE:
                logger.debug("收到来自服务端的推送mock规则消息, " + communicationMsg);
                processPushMockRule(ctx, communicationMsg);
                break;
            default:
                logger.error("invalid mock msg type");
                break;
        }
    }

    private void processPushMockRule(ChannelHandlerContext ctx, MockCommunicationMsg<?> communicationMsg) {
        PushMockRuleMsg<?> mockRuleMsg = ((PushMockRuleMsg<?>) communicationMsg.getBody()) ;
        UspMockAgent.application.getContext().getRuleFactory().putRule(mockRuleMsg);
        ctx.channel().writeAndFlush(new MockCommunicationMsg<>((byte) MockMsgType.PUSH_MOCK_RULE_ACK.val(),
                new BaseMockMsg(agentId, DateUtils.getDefaultDate())));
    }

    private void processRegisterAckMsg(MockCommunicationMsg<?> communicationMsg) {
        BaseMockMsg mockMsg = ((BaseMockMsg) communicationMsg.getBody());
        if(mockMsg == null) {
            logger.error("register error");
            return;
        }
        agentId = mockMsg.getAgentId();
    }

    private void processHeartbeatAckMsg() {
        lastReceiveHeartbeatAckTimestamp = System.currentTimeMillis();
    }

    public void sendHeartbeatMsg(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new MockCommunicationMsg<>((byte) MockMsgType.HEARTBEAT.val(),
                new BaseMockMsg(agentId, DateUtils.getDefaultDate())));
    }

    public void register(MockNettyClient nettyClient) {
        nettyClient.send(new MockCommunicationMsg<>((byte) MockMsgType.REGISTER.val(), mockAgentInstanceInfo));
    }

    public void requestMockRule(MockNettyClient nettyClient) {
        nettyClient.send(new MockCommunicationMsg<>((byte) MockMsgType.REQUEST_LOAD_MOCK_RULE.val(),
                new BaseMockMsg(agentId, DateUtils.getDefaultDate())));
    }

    public boolean isHeartbeatCheckTimeout() {
        return (System.currentTimeMillis() - lastReceiveHeartbeatAckTimestamp) >
                (long) MockNettyClientInitializer.READER_IDLE_TIME * 3 * 1000;
    }
}
