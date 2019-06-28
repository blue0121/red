package com.red.server.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private final Map<RegistryCommand, RegistryHandler> handlerMap = new HashMap<>();
	private final RegistryChannelGroup channelGroup;
	private final RegistryStorage registryStorage;

	private RegistryHandlerFactory()
	{
		channelGroup = new RegistryChannelGroup();
		registryStorage = new MemoryRegistryStorage(channelGroup);
		handlerMap.put(RegistryCommand.SAVE, new SaveRegistryHandler(registryStorage));
		handlerMap.put(RegistryCommand.DELETE, new DeleteRegistryHandler(registryStorage));
		handlerMap.put(RegistryCommand.LIST, new ListRegistryHandler(registryStorage));
		handlerMap.put(RegistryCommand.WATCH, new WatchRegistryHandler(registryStorage));
		handlerMap.put(RegistryCommand.UNWATCH, new UnwatchRegistryHandler(registryStorage));
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
		registryStorage.getExecutorService().submit(() ->
		{
			ResponseCode code = ResponseCode.SUCCESS;
			String msg = "Successful";
			boolean error = true;
			try
			{
				handler.handle(message, channel);
				error = false;
			}
			catch (RegistryStorageException e)
			{
				code = ResponseCode.REGISTRY;
				msg = e.getMessage();
			}
			catch (Throwable e)
			{
				code = ResponseCode.ERROR;
				msg = "Unknown";
				logger.error("Error: ", e);
			}
			if (error)
			{
				RegistryMessage response = message.toResponse(code, msg);
				channel.writeAndFlush(response);
			}
		});
	}

	public void disconnect(Channel channel)
	{
		registryStorage.getExecutorService().submit(() ->
		{
			try
			{
				registryStorage.disconnect(channel);
			}
			catch (Throwable e)
			{
				logger.error("Error: ", e);
			}
		});
	}

}
