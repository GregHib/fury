package com.fury.network.login.codec;

import com.fury.Main;
import com.fury.network.NetworkConstants;
import com.fury.network.login.LoginDetailsMessage;
import com.fury.network.login.LoginResponses;
import com.fury.network.login.LoginUtils;
import com.fury.network.security.IsaacRandom;
import com.fury.util.Misc;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public final class LoginDecoder extends ByteToMessageDecoder {

    private static final Random random = new SecureRandom();
    private int encryptedLoginBlockSize;
    private LoginDecoderState state = LoginDecoderState.LOGIN_REQUEST;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        switch(state) {
            case LOGIN_REQUEST:
                decodeRequest(ctx, buffer);
                break;
            case LOGIN_TYPE:
                decodeType(ctx, buffer);
                break;
            case LOGIN:
                decodeLogin(ctx, buffer, out);
                break;
        }
    }

    private void decodeRequest(ChannelHandlerContext ctx, ByteBuf buffer) {
        if(!buffer.isReadable()) {
            ctx.channel().close();
            return;
        }

        int request = buffer.readUnsignedByte();
        if (request != NetworkConstants.LOGIN_REQUEST_OPCODE) {
            Main.getLogger().info("Session rejected: invalid login request: " + request);
            LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_SERVER_OFFLINE);
            return;
        }

        ByteBuf buf = Unpooled.buffer(19);
        buf.writeByte(0);
        buf.writeLong(random.nextLong());
        ctx.writeAndFlush(buf);

        state = LoginDecoderState.LOGIN_TYPE;
    }

    private void decodeType(ChannelHandlerContext ctx, ByteBuf buffer) {
        if(!buffer.isReadable()) {
            ctx.channel().close();
            return;
        }

        int connectionType = buffer.readUnsignedByte();
        if (connectionType != NetworkConstants.NEW_CONNECTION_OPCODE
                && connectionType != NetworkConstants.RECONNECTION_OPCODE) {
            Main.getLogger().info("Session rejected: invalid login type: " + connectionType);
            LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_SERVER_OFFLINE);
            return;
        }

        state = LoginDecoderState.LOGIN;
    }

    private void decodeLogin(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if(!buffer.isReadable()) {
            ctx.channel().close();
            return;
        }

        encryptedLoginBlockSize = buffer.readUnsignedByte();

        if (buffer.readableBytes() != encryptedLoginBlockSize) {
            Main.getLogger().info("Invalid encryptedLoginBlockSize " + encryptedLoginBlockSize);
            LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_REJECT_SESSION);
            return;
        }
        if(buffer.isReadable(encryptedLoginBlockSize)) {
            int magicId = buffer.readUnsignedByte();
            if (magicId != 0xFF) {
                Main.getLogger().info("Invalid magic id: " + magicId);
                LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_REJECT_SESSION);
                return;
            }
            int clientVersion = buffer.readShort();
            int memory = buffer.readByte();
            if (memory != 0 && memory != 1) {
                Main.getLogger().info("Unhandled memory byte value: " + memory);
                LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_REJECT_SESSION);
                return;
            }

			/*int[] archiveCrcs = new int[9];
			for (int i = 0; i < 9; i++) {
				archiveCrcs[i] = buffer.readInt();
			}*/

            int length = buffer.readUnsignedByte();
            byte[] rsaBytes = new byte[length];
            buffer.readBytes(rsaBytes);
            /**
             * Our RSA components.
             */
            ByteBuf rsaBuffer = Unpooled.wrappedBuffer(new BigInteger(rsaBytes)
                    .modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS).toByteArray());

            int securityId = rsaBuffer.readByte();
            if (securityId != 10) {
                Main.getLogger().info("Invalid securityId: " + memory);
                LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_REJECT_SESSION);
                return;
            }

            long clientSeed = rsaBuffer.readLong();
            long seedReceived = rsaBuffer.readLong();

            int[] seed = new int[4];
            seed[0] = (int) (clientSeed >> 32);
            seed[1] = (int) clientSeed;
            seed[2] = (int) (seedReceived >> 32);
            seed[3] = (int) seedReceived;
            IsaacRandom decodingRandom = new IsaacRandom(seed);
            for (int i = 0; i < seed.length; i++)
                seed[i] += 50;

            int uid = rsaBuffer.readInt();

            String username = Misc.readString(rsaBuffer);
            String password = Misc.readString(rsaBuffer);
            String serial = Misc.readString(rsaBuffer);
            String hwid = Misc.readString(rsaBuffer);

            if (username.length() > 12 || password.length() > 20) {
                LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_INVALID_CREDENTIALS);
                return;
            }

            username = Misc.formatText(username.toLowerCase());

            out.add(new LoginDetailsMessage(ctx, username, password, LoginUtils.getHost(ctx.channel()), serial, hwid, clientVersion, uid, new IsaacRandom(seed), decodingRandom));
        }
    }

    private enum LoginDecoderState {
        LOGIN_REQUEST,
        LOGIN_TYPE,
        LOGIN
    }
}
