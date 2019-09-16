package com.red.server.registry;

import com.red.core.message.RegistryItem;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class MemoryRegistryStorage implements RegistryStorage
{
	private static Logger logger = LoggerFactory.getLogger(MemoryRegistryStorage.class);

	private final Map<String, Set<RegistryItem>> registryMap;
	private final RegistryChannelGroup channelGroup;

	public MemoryRegistryStorage(RegistryChannelGroup channelGroup)
	{
		this.registryMap = new HashMap<>();
		this.channelGroup = channelGroup;
	}

	@Override
	public void save(Set<String> nameSet, Channel channel)
	{
		if (nameSet == null || nameSet.isEmpty())
			throw new RegistryStorageException("nameSet is empty");
		RegistryItem item = channelGroup.getItem(channel);
		if (item == null)
			throw new RegistryStorageException("No bind channel");

		for (String name : nameSet)
		{
			Set<RegistryItem> set = registryMap.computeIfAbsent(name, k -> new HashSet<>());
			boolean isNotify = set.add(item);
			if (isNotify)
			{
				channelGroup.notify(name, set);
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Save registry message, nameSet: {}, item: {}", nameSet, item);
		}
	}

	@Override
	public void delete(Set<String> nameSet, Channel channel)
	{
		if (nameSet == null || nameSet.isEmpty())
			throw new RegistryStorageException("nameSet is empty");
		RegistryItem item = channelGroup.getItem(channel);
		if (item == null)
			throw new RegistryStorageException("No bind channel");

		for (String name : nameSet)
		{
			Set<RegistryItem> set = registryMap.get(name);
			if (set == null || set.isEmpty())
				continue;

			boolean isNotify = set.remove(item);
			if (isNotify)
			{
				channelGroup.notify(name, set);
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Delete registry message, name: {}, item: {}", nameSet, item);
		}
	}

	@Override
	public Set<RegistryItem> list(String name)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("name is empty");

		Set<RegistryItem> itemSet = new HashSet<>();
		Set<RegistryItem> set = registryMap.get(name);
		if (set != null && !set.isEmpty())
		{
			itemSet.addAll(set);
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("List registry message, name: {}, itemList: {}", name, itemSet);
		}
		return itemSet;
	}

	@Override
	public void disconnect(RegistryItem item, Channel channel)
	{
		item = channelGroup.unbindChannel(item, channel);
		//logger.info("disconnect, item: {}", item);
		if (item == null)
			return;

		for (Map.Entry<String, Set<RegistryItem>> entry : registryMap.entrySet())
		{
			Set<RegistryItem> itemSet = entry.getValue();
			if (itemSet == null || itemSet.isEmpty())
				continue;

			boolean result = itemSet.remove(item);
			if (result)
			{
				channelGroup.notify(entry.getKey(), itemSet);
			}
		}
	}

}
