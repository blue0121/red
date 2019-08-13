package com.red.server.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import com.red.server.queue.MessageChannel;
import com.red.server.queue.ScheduledQueue;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class RegistryHandlerFactory
{
	private static Logger logger = LoggerFactory.getLogger(RegistryHandlerFactory.class);
	private static volatile RegistryHandlerFactory factory;

	private final ScheduledQueue<RegistryMessage> queue;

	private RegistryHandlerFactory()
	{
		this.queue = new ScheduledQueue<>(new RegistryQueueHandler());
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
		MessageChannel<RegistryMessage> data = new MessageChannel<>(message, channel);
		this.queue.push(data);
		if (logger.isDebugEnabled())
		{
			logger.debug("push data, channel: {}, id: 0x{}, command: {}, name: {}, item: {}",
					channel.id(), Long.toHexString(message.getMessageId()),
					message.getCommand(), message.getNameSet(), message.getItemSet());
		}
	}

	public void disconnect(Channel channel)
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.UNBIND);
		this.handle(message, channel);
	}

}
