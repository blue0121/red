package blue.red.server.registry;

import blue.red.core.message.RegistryCommand;
import blue.red.core.message.RegistryItem;
import blue.red.core.message.RegistryMessage;
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
	private final Map<RegistryItem, ChannelId> itemIdMap;
	private final Map<ChannelId, RegistryItem> idItemMap;

	public RegistryChannelGroup()
	{
		this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		this.nameCache = new HashMap<>();
		this.itemIdMap = new HashMap<>();
		this.idItemMap = new HashMap<>();
	}

	public void bindChannel(RegistryItem item, Channel channel)
	{
		if (item == null)
			throw new RegistryChannelException("item is empty");

		if (itemIdMap.containsKey(item))
			throw new RegistryChannelException("This host has been bind: " + item);

		ChannelId id = channel.id();
		itemIdMap.put(item, id);
		idItemMap.put(id, item);
		channelGroup.add(channel);
		logger.info("Bind channel, host: {}, channel: {}", item, id);
	}

	public RegistryItem unbindChannel(RegistryItem item, Channel channel)
	{
		ChannelId id = channel.id();
		RegistryItem oldItem = idItemMap.get(id);
		if (oldItem == null)
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
		idItemMap.remove(id);
		itemIdMap.remove(oldItem);
		logger.info("Unbind channel [{}] from [{}]", id, oldItem);

		return oldItem;
	}

	public RegistryItem getItem(Channel channel)
	{
		return idItemMap.get(channel.id());
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

	public RegistryItem disconnect(Channel channel)
	{
		ChannelId id = channel.id();
		RegistryItem item = idItemMap.get(id);
		if (item == null)
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
		idItemMap.remove(id);
		itemIdMap.remove(item);
		logger.info("Unbind channel [{}] from [{}]", id, item);

		return item;
	}

	public void notify(String name, Collection<RegistryItem> itemList)
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

			logger.info("Notify {}, item: {}", name, itemList);
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
