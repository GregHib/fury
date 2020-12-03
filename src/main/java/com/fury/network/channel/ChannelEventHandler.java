package com.fury.network.channel;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.network.NetworkConstants;
import com.fury.network.PlayerSession;
import com.fury.network.SessionState;
import com.fury.network.login.LoginDetailsMessage;
import com.fury.network.packet.Packet;
import com.google.common.base.Objects;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class ChannelEventHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            PlayerSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();

            if (session == null) {
                throw new IllegalStateException("theSession == null");
            }

            if(msg instanceof LoginDetailsMessage) {
                session.finalizeLogin((LoginDetailsMessage)msg);
            } else if(msg instanceof Packet) {
                Packet packet = (Packet) msg;
                session.queuePacket(packet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        PlayerSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();

        if (session == null)
            throw new IllegalStateException("theSession == null");

        Player player = session.getPlayer();

        if(player == null)
            return;

        //Queue the player for logout
        if(session.getState() == SessionState.LOGGED_IN || session.getState() == SessionState.REQUESTED_LOG_OUT)
            GameWorld.queueLogout(player);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
                ctx.channel().close();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        if (NetworkConstants.IGNORED_NETWORK_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage())))
            e.printStackTrace();

        ctx.channel().close();
    }

}
