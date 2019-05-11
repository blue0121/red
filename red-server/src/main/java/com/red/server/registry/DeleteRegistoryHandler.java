package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class DeleteRegistoryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public DeleteRegistoryHandler(RegistryStorage storage)
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
			storage.delete(message.getName(), message.getItem());
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
