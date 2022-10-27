package com.tm.mockagent.entities.msg;

import lombok.Data;

@Data
public class MockCommunicationMsg<T> {
    // 0-心跳信息 1-心跳ack 2-注册 3-注册ack 4-请求加载mock配置信息
    // 5-请求加载mock配置ack 6-推送单个mock配置信息 7-推送单个mock配置信息ack
    // 8-启用一个规则 9-启用一个规则ack 10-禁用一个规则 11-禁用一个规则ack
    // 12-删除一个规则 13-删除一个规则ack
    private byte type;
    private T body;

    public MockCommunicationMsg(byte type, T body) {
        this.type = type;
        this.body = body;
    }
}
