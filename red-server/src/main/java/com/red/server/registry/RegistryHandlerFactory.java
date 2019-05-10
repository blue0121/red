package com.red.server.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class RegistryHandlerFactory
{
	private static Logger logger = LoggerFactory.getLogger(RegistryHandlerFactory.class);
	private static RegistryHandlerFactory factory;

	private Map<RegistryCommand, RegistryHandler> handlerMap = new HashMap<>();

	private RegistryHandlerFactory()
	{

	}

	public static RegistryHandlerFactory getFactory()
	{
		if (factory == null)
		{
			synchronized (RegistryHandlerFactory.class)
			{
				if (factory == null)
				{
					factory = new RegistryHandlerFactory();
				}
			}
		}
		return factory;
	}

	public void handle(RegistryMessage message, Channel channel)
	{
		RegistryHandler handler = handlerMap.get(message.getCommand());
		if (handler == null)
		{
			logger.error("Missing RegistryHandler: {}", message.getCommand());
			return;
		}
		handler.handle(message, channel);
	}

}
