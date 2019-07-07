package com.red.server.cache;

import com.red.core.message.CacheMessage;
import com.red.core.message.RegistryCommand;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class CacheHandlerFactory
{
	private static Logger logger = LoggerFactory.getLogger(CacheHandlerFactory.class);
	private static CacheHandlerFactory factory;

	private final Map<RegistryCommand, CacheHandler> handlerMap = new HashMap<>();
	private final CacheStorage cacheStorage;

	private CacheHandlerFactory()
	{
		this.cacheStorage = new MemoryCacheStorage();
	}

	public static CacheHandlerFactory getFactory()
	{
		if (factory == null)
		{
			synchronized (CacheHandlerFactory.class)
			{
				if (factory == null)
				{
					factory = new CacheHandlerFactory();
				}
			}
		}
		return factory;
	}

	public void handle(CacheMessage message, Channel channel)
	{
		CacheHandler handler = handlerMap.get(message.getCommand());
		if (handler == null)
		{
			logger.error("Missing RegistryHandler: {}", message.getCommand());
			return;
		}
		cacheStorage.getExecutorService().submit(() ->
		{
			ResponseCode code = ResponseCode.SUCCESS;
			String msg = "Successful";
			boolean error = true;
			try
			{
				handler.handle(message, channel);
				error = false;
			}
			catch (CacheStorageException e)
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
				CacheMessage response = message.toResponse(code, msg);
				channel.writeAndFlush(response);
			}
		});
	}

}
