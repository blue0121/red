package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class UnwatchRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public UnwatchRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		storage.unwatch(message.getName(), channel);
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.name());
		channel.writeAndFlush(response);
	}
}