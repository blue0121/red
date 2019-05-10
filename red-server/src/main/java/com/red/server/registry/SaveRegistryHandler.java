package com.red.server.registry;

import com.red.core.message.RegistryMessage;

import java.nio.channels.Channel;

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

	}
}
