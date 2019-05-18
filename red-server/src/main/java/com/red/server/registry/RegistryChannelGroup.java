package com.red.server.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class RegistryChannelGroup
{
	private static Logger logger = LoggerFactory.getLogger(RegistryChannelGroup.class);

	private final ChannelGroup channelGroup;
	private final Map<String, Set<ChannelId>> nameCache;

	public RegistryChannelGroup()
	{
		this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		this.nameCache = new HashMap<>();
	}

	public void addChannel(String name, Channel channel)
	{
		channelGroup.add(channel);
		Set<ChannelId> idSet = nameCache.get(name);
		if (idSet == null)
		{
			idSet = new HashSet<>();
			nameCache.put(name, idSet);
		}
		idSet.add(channel.id());
		logger.debug("Bind channel [{}] to [{}]", channel.id(), name);
	}

	public void removeChannel(String name, Channel channel)
	{
		Set<ChannelId> idSet = nameCache.get(name);
		if (idSet == null)
			return;

		boolean result = idSet.remove(channel.id());
		if (result)
		{
			logger.debug("Unbind channel [{}] from [{}]", channel.id(), name);
		}
	}

	public void removeChannel(Channel channel)
	{
		for (Map.Entry<String, Set<ChannelId>> entry : nameCache.entrySet())
		{
			Set<ChannelId> idSet = entry.getValue();
			if (idSet == null || idSet.isEmpty())
				continue;

			boolean result = idSet.remove(channel.id());
			if (result)
			{
				logger.debug("Remove channel [{}] from [{}]", channel.id(), entry.getKey());
			}
		}
	}

	public void notify(String name, Collection<String> itemList)
	{
		Set<ChannelId> idSet = nameCache.get(name);
		if (idSet == null || idSet.isEmpty())
		{
			logger.debug("No channel bind [{}]", name);
			return;
		}

		RegistryMessage message = RegistryMessage.create(RegistryCommand.WATCH, name);
		message.addItemList(itemList);
		for (ChannelId id : idSet)
		{
			Channel channel = channelGroup.find(id);
			if (channel == null)
				continue;

			channel.writeAndFlush(message).addListener(new WatchListener(message));
		}
	}

	class WatchListener implements ChannelFutureListener
	{
		private final RegistryMessage message;

		public WatchListener(RegistryMessage message)
		{
			this.message = message;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception
		{
			if (future.isSuccess())
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Notify registry message successful: name: {}, itemList: {}", message.getName(), message.getItemList());
				}
			}
			else
			{
				logger.warn("Notify registry message failure: {}, itemList: {}", message.getName(), message.getItemList());
			}
			Throwable throwable = future.cause();
			if (throwable != null)
			{
				logger.error("Occur exception: ", throwable);
			}
		}
	}

}
