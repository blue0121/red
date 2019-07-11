package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;


/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class SaveRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public SaveRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getNameSet().isEmpty())
			throw new RegistryStorageException("name is empty");

		storage.save(message.getNameSet(), channel);
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Save successful");
		channel.writeAndFlush(response);
	}

}
