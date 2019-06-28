package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class SaveRegistryHandler implements RegistryHandler
{
	private static Logger logger = LoggerFactory.getLogger(SaveRegistryHandler.class);

	private final RegistryStorage storage;

	public SaveRegistryHandler(RegistryStorage storage)
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
			storage.save(message.getNameSet(), item, channel);
		}
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Save successful");
		channel.writeAndFlush(response);
	}

}
