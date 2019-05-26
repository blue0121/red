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
		ResponseCode code = ResponseCode.SUCCESS;
		String msg = "Save successful";
		try
		{
			storage.save(message.getNameSet(), message.getItem(), channel);
		}
		catch (RegistryStorageException e)
		{
			code = ResponseCode.REGISTRY;
			msg = e.getMessage();
		}
		catch (Exception e)
		{
			code = ResponseCode.ERROR;
			msg = "Unknown exception";
		}
		RegistryMessage response = message.toResponse(code, msg);
		channel.writeAndFlush(response);
	}

}
