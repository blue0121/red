package blue.red.server.cache;

import blue.red.core.message.CacheCommand;
import blue.red.core.message.CacheMessage;
import blue.red.core.message.ResponseCode;
import blue.red.server.queue.MessageChannel;
import blue.red.server.queue.QueueHandler;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Jin Zheng
 * @since 1.0 2019-07-25
 */
public class CacheQueueHandler implements QueueHandler<CacheMessage>
{
	private static Logger logger = LoggerFactory.getLogger(CacheQueueHandler.class);

	private final Map<CacheCommand, CacheHandler> handlerMap = new HashMap<>();
	private final CacheStorage cacheStorage;

	public CacheQueueHandler()
	{
		this.cacheStorage = new MemoryCacheStorage();
		handlerMap.put(CacheCommand.GET, new GetCacheHandler(cacheStorage));
		handlerMap.put(CacheCommand.SET, new SetCacheHandler(cacheStorage));
		handlerMap.put(CacheCommand.DELETE, new DeleteCacheHandler(cacheStorage));
	}

	@Override
	public void handle(MessageChannel<CacheMessage> data)
	{
		CacheMessage message = data.getMessage();
		Channel channel = data.getChannel();
		CacheHandler handler = handlerMap.get(message.getCommand());
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
