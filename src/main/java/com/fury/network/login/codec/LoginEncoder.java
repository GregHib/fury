package com.fury.network.login.codec;

import com.fury.network.login.LoginResponsePacket;
import com.fury.network.login.LoginResponses;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LoginEncoder extends MessageToByteEncoder<LoginResponsePacket> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LoginResponsePacket msg, ByteBuf out) throws Exception {
        out.writeByte(msg.getResponse());
        if (msg.getResponse() == LoginResponses.LOGIN_SUCCESSFUL)
            out.writeByte(msg.getRights());
    }
}
