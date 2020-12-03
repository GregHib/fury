package com.fileserver.net;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.fileserver.FileServer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * An {@link SimpleChannelInboundHandler} for the {@link FileServer}.
 * Manages a channel's events.
 * 
 * @author Professor Oak
 */
@Sharable
public final class ChannelHandler extends SimpleChannelInboundHandler<Object> {
	/**
	 * The list of exceptions that are ignored and discarded.
	 */
	public static final ImmutableList<String> IGNORED_NETWORK_EXCEPTIONS =
			ImmutableList.of("An existing connection was forcibly closed by the remote host",
					"An established connection was aborted by the software in your host machine",
					"En befintlig anslutning tvingades att st�nga av fj�rrv�rddatorn"); //Swedish <3

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if (!IGNORED_NETWORK_EXCEPTIONS.stream()
				.anyMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.channel().close();
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
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
	}
}
