package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-11
 */
public class UnbindRegistryHandler implements RegistryHandler
{
	private final RegistryChannelGroup channelGroup;

	public UnbindRegistryHandler(RegistryChannelGroup channelGroup)
	{
		this.channelGroup = channelGroup;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getItemSet().size() != 1)
			throw new RegistryChannelException("item size must be 1");

		channelGroup.unbindChannel(message.getItem(), channel);
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Unbind successful");
		channel.writeAndFlush(response);
	}
}
