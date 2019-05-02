package com.red.core.handler;

import com.red.core.message.HeartbeatMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<HeartbeatMessage>
{
	private static Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

	private InetSocketAddress remoteAddress;

	public HeartbeatHandler()
	{
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage message) throws Exception
	{
		if (message.isPing())
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Receive PING: {} from {}", Long.toHexString(message.getMessageId()), remoteAddress);
			}
			HeartbeatMessage pong = message.toPong();
			ctx.writeAndFlush(pong);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
	{
		if (evt instanceof IdleStateEvent)
		{
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE)
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Read idle, no receive PONG, close!");
				}
				ctx.close();
			}
			else if (event.state() == IdleState.WRITER_IDLE)
			{
				HeartbeatMessage ping = HeartbeatMessage.createPing();
				ctx.writeAndFlush(ping);
				if (logger.isDebugEnabled())
				{
					logger.debug("Send PING: {} to {}", Long.toHexString(ping.getMessageId()), remoteAddress);
				}
			}
		}

		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		Channel ch = ctx.channel();
		remoteAddress = (InetSocketAddress) ch.remoteAddress();

		super.channelActive(ctx);
	}
}
