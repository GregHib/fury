package com.fury.network.channel;

import com.fury.network.NetworkConstants;
import com.fury.network.PlayerSession;
import com.fury.network.login.codec.LoginDecoder;
import com.fury.network.login.codec.LoginEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ChannelPipelineHandler extends ChannelInitializer<SocketChannel> {

    private final ChannelFilter FILTER = new ChannelFilter();

    private final ChannelEventHandler HANDLER = new ChannelEventHandler();

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();

        channel.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new PlayerSession(channel));

        pipeline.addLast("channel-filter", FILTER);
        pipeline.addLast("decoder", new LoginDecoder());
        pipeline.addLast("encoder", new LoginEncoder());
        pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.SESSION_TIMEOUT, 0, 0));
        pipeline.addLast("channel-handler", HANDLER);
    }
}
