package com.red.server.cache;

import com.red.core.message.CacheMessage;
import com.red.core.message.ResponseCode;
import com.red.server.config.RedConfig;
import com.red.server.config.RedConfigItem;
import com.red.server.queue.MessageChannel;
import com.red.server.queue.QueueHandler;
import com.red.server.queue.ScheduledQueue;
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
		logger.debug("push data, key: {}", message.getKey());
	}

}
