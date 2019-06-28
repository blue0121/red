package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class DeleteRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public DeleteRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getNameSet().isEmpty() || message.getItemSet().isEmpty())
			throw new RegistryStorageException("name or item is empty");

		for (String item : message.getItemSet())
		{
			storage.delete(message.getNameSet(), item, channel);
		}
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Delete successful");
		channel.writeAndFlush(response);
	}
}
