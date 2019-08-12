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
	private final Map<String, ChannelId> itemMap;
	private final Map<ChannelId, String> idMap;

	public RegistryChannelGroup()
	{
		this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		this.nameCache = new HashMap<>();
		this.itemMap = new HashMap<>();
		this.idMap = new HashMap<>();
	}

	public void bindChannel(String item, Channel channel)
	{
		if (item == null || item.isEmpty())
			throw new RegistryChannelException("item is empty");

		if (itemMap.containsKey(item))
			throw new RegistryChannelException("This host has been bind: " + item);

		ChannelId id = channel.id();
		itemMap.put(item, id);
		idMap.put(id, item);
		channelGroup.add(channel);
		logger.info("Bind channel, host: {}, channel: {}", item, id);
	}

	public String unbindChannel(String item, Channel channel)
	{
		ChannelId id = channel.id();
		String oldItem = idMap.get(id);
		if (oldItem == null || oldItem.isEmpty())
			return oldItem;

		if (item != null && !item.equals(oldItem))
			throw new RegistryChannelException("Can not unbind host: " + item);

		for (Map.Entry<String, Set<ChannelId>> entry : nameCache.entrySet())
		{
			Set<ChannelId> idSet = entry.getValue();
			if (idSet == null || idSet.isEmpty())
				continue;

			boolean result = idSet.remove(id);
			if (result)
			{
				logger.info("Disconnect channel [{}] from [{}]", channel.id(), entry.getKey());
			}
		}
		idMap.remove(id);
		itemMap.remove(oldItem);
		logger.info("Unbind channel [{}] from [{}]", id, oldItem);

		return oldItem;
	}

	public String getItem(Channel channel)
	{
		return idMap.get(channel.id());
	}

	public void watchChannel(String name, Channel channel)
	{
		channelGroup.add(channel);
		Set<ChannelId> idSet = nameCache.computeIfAbsent(name, k -> new HashSet<>());
		idSet.add(channel.id());
		logger.info("Watch channel [{}] to [{}]", channel.id(), name);
	}

	public void unwatchChannel(String name, Channel channel)
	{
		Set<ChannelId> idSet = nameCache.get(name);
		if (idSet == null)
			return;

		boolean result = idSet.remove(channel.id());
		if (result)
		{
			logger.info("Unwatch channel [{}] from [{}]", channel.id(), name);
		}
	}

	public String disconnect(Channel channel)
	{
		ChannelId id = channel.id();
		String item = idMap.get(id);
		if (item == null || item.isEmpty())
			return item;

		for (Map.Entry<String, Set<ChannelId>> entry : nameCache.entrySet())
		{
			Set<ChannelId> idSet = entry.getValue();
			if (idSet == null || idSet.isEmpty())
				continue;

			boolean result = idSet.remove(id);
			if (result)
			{
				logger.info("Disconnect channel [{}] from [{}]", channel.id(), entry.getKey());
			}
		}
		idMap.remove(id);
		itemMap.remove(item);
		logger.info("Unbind channel [{}] from [{}]", id, item);

		return item;
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
					logger.debug("Notify registry message successful: name: {}, itemList: {}", message.getName(), message.getItemSet());
				}
			}
			else
			{
				logger.warn("Notify registry message failure: {}, itemList: {}", message.getName(), message.getItemSet());
			}
			Throwable throwable = future.cause();
			if (throwable != null)
			{
				logger.error("Occur exception: ", throwable);
			}
		}
	}

}
