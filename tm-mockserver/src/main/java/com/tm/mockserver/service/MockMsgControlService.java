package com.tm.mockserver.service;

import com.tm.common.base.mapper.MockAgentInstanceMapper;
import com.tm.common.base.mapper.MockRuleAgentRelationMapper;
import com.tm.common.base.model.HttpMockRule;
import com.tm.common.base.model.MockAgentInstance;
import com.tm.common.entities.mock.msg.*;
import com.tm.common.utils.DateUtils;
import com.tm.mockserver.netty.mock.ChannelHandlerContextFactory;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
public class MockMsgControlService {

    @Autowired
    private ChannelHandlerContextFactory channelHandlerContextFactory;

    @Autowired
    private MockAgentInstanceMapper mockAgentInstanceMapper;

    @Autowired
    private MockRuleAgentRelationMapper mockRuleAgentRelationMapper;

    @Getter
    private Map<Integer, Long> lastHeartbeatTimestampMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        mockAgentInstanceMapper.setAllAgentToOffline();
    }

    public void process(@NotNull MockCommunicationMsg<Object> communicationMsg, @NotNull ChannelHandlerContext ctx) {
        final MockMsgType mockMsgType = MockMsgType.get(communicationMsg.getType());
        Objects.requireNonNull(mockMsgType, "invalid mock msg type");

        switch (mockMsgType) {
            case HEARTBEAT:
                log.info("收到来自客户端的心跳消息：{}", communicationMsg.getBody());
                log.info("回复一个心跳ack");
                processHeartbeatMsg(communicationMsg, ctx);
                break;
            case HEARTBEAT_ACK:
                log.info("收到来自客户端的心跳ack消息");
                break;
            case REGISTER:
                log.info("收到来自客户端的注册消息：{}", communicationMsg.getBody());
                processRegister(communicationMsg, ctx);
                break;
            case REGISTER_ACK:
                log.info("收到来自客户端的注册ack消息");
                break;
            case PUSH_MOCK_RULE_ACK:
                log.info("收到来自客户端的推送mock规则的ack消息");
                break;
            case REQUEST_LOAD_MOCK_RULE:
                log.info("收到来自客户端的请求加载mock规则的消息");
                processLoadMockRule(communicationMsg, ctx);
                break;
            case REQUEST_LOAD_MOCK_RULE_ACK:
                log.info("收到来自客户端的请求加载mock规则ack消息");
                break;
            case ENABLE_MOCK_RULE:
                log.info("收到来自客户端的启用mock规则的信息");
                break;
            case ENABLE_MOCK_RULE_ACK:
                log.info("收到来自客户端的启用mock规则的ack消息");
                break;
            case DISABLE_MOCK_RULE:
                log.info("收到来自客户端的禁用mock规则的消息");
                break;
            case DISABLE_MOCK_RULE_ACK:
                log.info("收到来自客户端的禁用mock规则的ack消息");
                break;
            case REMOVE_MOCK_RULE:
                log.info("收到来自客户端的删除mock规则的消息");
                break;
            case REMOVE_MOCK_RULE_ACK:
                log.info("收到来自客户端的删除mock规则的ack消息");
                break;
            default:
                log.info("invalid msg type");
                break;
        }
    }

    private void processLoadMockRule(MockCommunicationMsg<Object> communicationMsg, ChannelHandlerContext ctx) {
        BaseMockMsg mockMsg = (BaseMockMsg) communicationMsg.getBody();
        final MockAgentInstance mockAgentInstance = mockAgentInstanceMapper.selectByPrimaryKey(mockMsg.getAgentId());
        Objects.requireNonNull(mockAgentInstance, "invalid mock agent id " + mockMsg.getAgentId());

        final List<HttpMockRule> rules = mockRuleAgentRelationMapper.selectAllHttpMockRules(mockAgentInstance.getId());
        if(rules == null || rules.isEmpty()) {
            log.info("mock agent id: {}, 没有配置mock规则", mockAgentInstance.getId());
        }
        for (HttpMockRule rule : rules) {
            pushMockRule(ctx, rule);
        }
        ctx.flush();
    }

    private void pushMockRule(ChannelHandlerContext ctx, HttpMockRule rule) {
        PushMockRuleMsg<HttpMockRule> msg = new PushMockRuleMsg<>();
        msg.setMockRule(rule);
        msg.setMockRuleId(rule.getId());
        msg.setMockRuleType(1);
        ctx.channel().write(new MockCommunicationMsg<>((byte) MockMsgType.PUSH_MOCK_RULE.val(), msg));
    }

    private void processRegister(@NotNull MockCommunicationMsg<Object> communicationMsg, @NotNull ChannelHandlerContext ctx) {
        MockAgentInstanceInfo mockMsg = ((MockAgentInstanceInfo) communicationMsg.getBody());
        if(mockMsg == null) {
            log.error("register error");
            return;
        }
        if(mockMsg.getIp() == null || mockMsg.getPort() == null) {
            log.error("register error, ip or port is null");
            return;
        }
        MockAgentInstance register = register(mockMsg);
        channelHandlerContextFactory.registerAgent(register.getId(), ctx);

        BaseMockMsg returnMsg = new BaseMockMsg(register.getId(), DateUtils.getDefaultDate());
        ctx.channel().writeAndFlush(new MockCommunicationMsg<>((byte) MockMsgType.REGISTER_ACK.val(), returnMsg));
    }

    private MockAgentInstance register(MockAgentInstanceInfo mockMsg) {
        MockAgentInstance mockAgentInstance = mockAgentInstanceMapper.selectByIpAndPort(mockMsg.getIp(), mockMsg.getPort());
        if(mockAgentInstance == null) {
            mockAgentInstance = new MockAgentInstance();
            mockAgentInstance.setApplicationName(mockMsg.getApplicationName());
            mockAgentInstance.setIp(mockMsg.getIp());
            mockAgentInstance.setPort(mockMsg.getPort());
            mockAgentInstance.setName(mockMsg.getName());
            mockAgentInstance.setDescription(mockMsg.getDescription());
            mockAgentInstance.setFirstRegisterTime(new Date());
            mockAgentInstance.setLastRegisterTime(new Date());
            mockAgentInstance.setStatus(1);
            mockAgentInstanceMapper.insertBySelective(mockAgentInstance);
        }else{
            mockAgentInstance.setApplicationName(mockMsg.getApplicationName());
            mockAgentInstance.setName(mockMsg.getName());
            mockAgentInstance.setDescription(mockMsg.getDescription());
            mockAgentInstance.setLastRegisterTime(new Date());
            mockAgentInstance.setStatus(1);
            mockAgentInstanceMapper.updateBySelective(mockAgentInstance);
        }

        return mockAgentInstance;
    }

    private void processHeartbeatMsg(@NotNull MockCommunicationMsg<Object> communicationMsg,
                                     @NotNull ChannelHandlerContext ctx) {
        BaseMockMsg mockMsg = (BaseMockMsg) communicationMsg.getBody();
        mockAgentInstanceMapper.lightenAgent(mockMsg.getAgentId());
        channelHandlerContextFactory.registerAgent(mockMsg.getAgentId(), ctx);

        mockMsg.setActionTime(DateUtils.getDefaultDate());
        ctx.channel().writeAndFlush(new MockCommunicationMsg<>((byte) MockMsgType.HEARTBEAT_ACK.val(), mockMsg));
        if(mockMsg.getAgentId() != null) {
            log.info("记录mock agent的最后一次心跳时间");
            lastHeartbeatTimestampMap.put(mockMsg.getAgentId(), System.currentTimeMillis());
        }
    }

    public boolean isHeartbeatCheckTimeout(Integer agentId) {
        final Long aLong = lastHeartbeatTimestampMap.get(agentId);
        if(aLong != null) {
            return (System.currentTimeMillis() - aLong) > 10 * 6 * 1000;
        }
        return false;
    }

    public void setAgentToOffline(Integer id) {
        mockAgentInstanceMapper.setAgentToOffline(id);
    }

    @Async
    @Scheduled(cron = "0 */2 * * * ?")
    public void scanAgentStatus() {
        lastHeartbeatTimestampMap.forEach((key, value) -> {
            if(isHeartbeatCheckTimeout(key)) {
                log.debug("mock agent {} 心跳超时,状态设置为离线", key);
                setAgentToOffline(key);
            }
        });
    }
}
