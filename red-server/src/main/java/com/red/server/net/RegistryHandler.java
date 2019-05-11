package com.red.server.net;

import com.red.core.message.RegistryMessage;
import com.red.server.registry.RegistryHandlerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public class RegistryHandler extends SimpleChannelInboundHandler<RegistryMessage>
{
	private static Logger logger = LoggerFactory.getLogger(RegistryHandler.class);

	public RegistryHandler()
	{
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RegistryMessage message) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Receive registry message, command: {}, name: {}, item: {}", message.getCommand(), message.getName(), message.getItemList());
		}
		RegistryHandlerFactory.getFactory().handle(message, ctx.channel());
	}

}
