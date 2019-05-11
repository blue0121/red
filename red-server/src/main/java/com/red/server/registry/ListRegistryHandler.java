package com.red.server.registry;

import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class ListRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public ListRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		ResponseCode code = ResponseCode.SUCCESS;
		String msg = "Save successful";
		Set<String> itemSet = null;
		try
		{
			itemSet = storage.list(message.getName());
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
		if (itemSet != null && !itemSet.isEmpty())
		{
			for (String item : itemSet)
			{
				response.addItem(item);
			}
		}
		channel.writeAndFlush(response);
	}
}
