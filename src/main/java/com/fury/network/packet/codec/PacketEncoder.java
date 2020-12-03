package com.fury.network.packet.codec;

import com.fury.network.packet.Packet;
import com.fury.network.security.IsaacRandom;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    private final IsaacRandom encoder;

    public PacketEncoder(IsaacRandom encoder) {
        this.encoder = encoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        final int opcode = (packet.getOpcode() + encoder.nextInt()) & 0xFF;
        final int size = packet.getSize();
        final ByteBuf buf = packet.getBuffer();

        out.writeByte(opcode);
        out.writeShort(size);
        out.writeBytes(buf);
    }
}
