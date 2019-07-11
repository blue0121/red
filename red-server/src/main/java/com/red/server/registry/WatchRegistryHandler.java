package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;


/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class WatchRegistryHandler implements RegistryHandler
{
	private final RegistryChannelGroup channelGroup;

	public WatchRegistryHandler(RegistryChannelGroup channelGroup)
	{
		this.channelGroup = channelGroup;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getNameSet().isEmpty())
			throw new RegistryStorageException("name is empty");

		for (String name : message.getNameSet())
		{
			channelGroup.watchChannel(name, channel);
		}
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Watch successful");
		channel.writeAndFlush(response);
	}
}
