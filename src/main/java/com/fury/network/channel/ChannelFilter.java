package com.fury.network.channel;

import com.fury.game.GameSettings;
import com.fury.network.NetworkConstants;
import com.fury.network.login.LoginResponses;
import com.fury.network.login.LoginUtils;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ChannelFilter extends ChannelInboundHandlerAdapter {

    /**
     * The {@link Multiset} of connections currently active within the server.
     */
    private final Multiset<String> connections = ConcurrentHashMultiset.create();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String host = LoginUtils.getHost(ctx.channel());

        // if this local then, do nothing and proceed to next handler in the pipeline.
        if (!GameSettings.HOSTED)
            return;

        // evaluate the amount of connections from this host.
        if (connections.count(host) > NetworkConstants.CONNECTION_LIMIT) {
            LoginUtils.sendResponseCode(ctx, LoginResponses.LOGIN_CONNECTION_LIMIT);
            return;
        }

        // add the host
        connections.add(host);

        // Nothing went wrong, so register the channel and forward the event to next handler in the pipeline.
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        String host = LoginUtils.getHost(ctx.channel());

        // if this is local, do nothing and proceed to next handler in the pipeline.
        if (!GameSettings.HOSTED)
            return;

        // remove the host from the connection list
        connections.remove(host);

        // the connection is unregistered so forward the event to the next handler in the pipeline.
        ctx.fireChannelUnregistered();
    }


}
