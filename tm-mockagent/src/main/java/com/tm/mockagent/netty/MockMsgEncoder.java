package com.tm.mockagent.netty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tm.mockagent.entities.msg.MockCommunicationMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;

public class MockMsgEncoder extends MessageToByteEncoder<MockCommunicationMsg> {
    public static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          MockCommunicationMsg mockCommunicationMsg,
                          ByteBuf byteBuf) throws Exception {
        Objects.requireNonNull(mockCommunicationMsg);
        byte[] bytes = gson.toJson(mockCommunicationMsg.getBody()).getBytes();
        byteBuf.writeByte(mockCommunicationMsg.getType());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
