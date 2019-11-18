package blue.red.server.cache;

import blue.red.core.message.CacheMessage;
import blue.red.core.message.ResponseCode;
import blue.red.server.config.RedConfig;
import blue.red.server.config.RedConfigItem;
import blue.red.server.queue.MessageChannel;
import blue.red.server.queue.QueueHandler;
import blue.red.server.queue.ScheduledQueue;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class CacheHandlerFactory
{
	private static Logger logger = LoggerFactory.getLogger(CacheHandlerFactory.class);
	private static volatile CacheHandlerFactory factory;

	private final RedConfig redConfig = RedConfig.getInstance();
	private final ScheduledQueue<CacheMessage> queue;

	private CacheHandlerFactory()
	{
		List<QueueHandler<CacheMessage>> queueHandlerList = new ArrayList<>();
		int partition = redConfig.getInt(RedConfigItem.CACHE_MEMORY_PARTITION, RedConfigItem.CACHE_MEMORY_PARTITION_VALUE);
		for (int i = 0; i < partition; i++)
		{
			queueHandlerList.add(new CacheQueueHandler());
		}
		this.queue = new ScheduledQueue<>(queueHandlerList);
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
		if (message.getKey() == null || message.getKey().isEmpty())
		{
			CacheMessage response = message.toResponse(ResponseCode.CACHE, "key is empty");
			channel.writeAndFlush(response);
			return;
		}

		MessageChannel<CacheMessage> data = new MessageChannel<>(message, channel);
		this.queue.push(message.getKey(), data);
		int size = message.getValue() == null ? 0 : message.getValue().length;
		logger.debug("push data, channel: {}, id: 0x{}, command: {}, key: {}, value size: {}",
				channel.id(), Long.toHexString(message.getMessageId()),
				message.getCommand(), message.getKey(), size);
	}

}
