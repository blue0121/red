package com.red.server.registry;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class MemoryRegistryStorage implements RegistryStorage
{
	private static Logger logger = LoggerFactory.getLogger(MemoryRegistryStorage.class);

	private final Map<String, Set<String>> registryMap;
	private final RegistryChannelGroup channelGroup;
	private final ExecutorService executorService;

	public MemoryRegistryStorage(RegistryChannelGroup channelGroup)
	{
		this.registryMap = new HashMap<>();
		this.channelGroup = channelGroup;
		this.executorService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void save(Set<String> nameSet, Channel channel)
	{
		if (nameSet == null || nameSet.isEmpty())
			throw new RegistryStorageException("nameSet is empty");
		String item = channelGroup.getItem(channel);
		if (item == null || item.isEmpty())
			throw new RegistryStorageException("No bind channel");

		for (String name : nameSet)
		{
			Set<String> set = registryMap.computeIfAbsent(name, k -> new HashSet<>());
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
		String item = channelGroup.getItem(channel);
		if (item == null || item.isEmpty())
			throw new RegistryStorageException("No bind channel");

		for (String name : nameSet)
		{
			Set<String> set = registryMap.get(name);
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
	public Set<String> list(String name)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("name is empty");

		Set<String> itemSet = new HashSet<>();
		Set<String> set = registryMap.get(name);
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
	public void disconnect(String item, Channel channel)
	{
		item = channelGroup.unbindChannel(item, channel);
		if (item == null || item.isEmpty())
			return;

		for (Map.Entry<String, Set<String>> entry : registryMap.entrySet())
		{
			Set<String> itemSet = entry.getValue();
			if (itemSet == null || itemSet.isEmpty())
				continue;

			boolean result = itemSet.remove(item);
			if (result)
			{
				channelGroup.notify(entry.getKey(), itemSet);
			}
		}
	}

	@Override
	public ExecutorService getExecutorService()
	{
		return executorService;
	}

}
