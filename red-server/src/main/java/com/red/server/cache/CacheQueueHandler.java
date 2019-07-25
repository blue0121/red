package com.red.server.cache;

import com.red.core.message.CacheMessage;
import com.red.core.message.ResponseCode;
import com.red.server.queue.MessageChannel;
import com.red.server.queue.QueueHandler;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jin Zheng
 * @since 1.0 2019-07-25
 */
public class CacheQueueHandler implements QueueHandler<CacheMessage>
{
	private static Logger logger = LoggerFactory.getLogger(CacheQueueHandler.class);
	private CacheHandlerFactory factory;

	public CacheQueueHandler()
	{
		this.factory = CacheHandlerFactory.getFactory();
	}

	@Override
	public void handle(MessageChannel<CacheMessage> data)
	{
		CacheMessage message = data.getMessage();
		Channel channel = data.getChannel();
		CacheHandler handler = factory.getHandler(message.getCommand());
		if (handler == null)
		{
			logger.error("Missing RegistryHandler: {}", message.getCommand());
			return;
		}

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
			code = ResponseCode.CACHE;
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
	}
}
