package com.tm.mockagent.netty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tm.mockagent.entities.model.HttpMockRule;
import com.tm.mockagent.entities.model.MockAgentInstanceInfo;
import com.tm.mockagent.entities.msg.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MockMsgDecoder extends ByteToMessageDecoder {
    public static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        byte type = in.readByte();
        int length = in.readInt();
        String body = null;
        if (length > 0){
            ByteBuf buf = in.readBytes(length);
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            body = new String(req, StandardCharsets.UTF_8);
        }
        MockCommunicationMsg requestParam;
        Object msg = null;
        switch (type) {
                // 心跳信息
            case 0:
                // 心跳ack
            case 1:
                // 注册ack
            case 3:
                // 请求加载mock配置信息
            case 4:
                // 请求加载mock配置ack
            case 5:
                // 推送单个mock配置信息ack
            case 7:
                msg = gson.fromJson(body, BaseMockMsg.class);
                requestParam = new MockCommunicationMsg<>(type, (BaseMockMsg) msg);
                break;
                // 启用一个规则
            case 8:
                // 启用一个规则ack
            case 9:
                // 禁用一个规则
            case 10:
                // 禁用一个规则ack
            case 11:
                // 删除一个规则
            case 12:
                // 删除一个规则ack
            case 13:
                msg = gson.fromJson(body, MockRulePairMsg.class);
                requestParam = new MockCommunicationMsg<>(type, (MockRulePairMsg) msg);
                break;
                // 注册
            case 2:
                msg = gson.fromJson(body, MockAgentInstanceInfo.class);
                requestParam = new MockCommunicationMsg<>(type, (MockAgentInstanceInfo) msg);
                break;
                // 推送单个mock配置信息
            case 6:
                msg = gson.fromJson(body, PushMockRuleMsg.class);
                if(1 == (((PushMockRuleMsg<?>) msg).getMockRuleType())) {
                    msg = gson.fromJson(body, new TypeToken<PushMockRuleMsg<HttpMockRule>>() {}.getType());
                }else{
                    msg = null;
                }
                requestParam = new MockCommunicationMsg<>(type, (PushMockRuleMsg<?>)msg);
                break;
            default:
                requestParam = new MockCommunicationMsg<>(type, null);
                break;
        }

        out.add(requestParam);
    }
}
