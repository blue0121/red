package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

import java.util.Set;


/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class WatchRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public WatchRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		storage.watch(message.getName(), channel);
		Set<String> itemSet = storage.list(message.getName());
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.name());
		response.addItemList(itemSet);
		channel.writeAndFlush(response);
	}
}