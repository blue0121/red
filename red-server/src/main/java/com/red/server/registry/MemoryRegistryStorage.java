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
	public void save(Set<String> nameSet, String item, Channel channel)
	{
		if (nameSet == null || nameSet.isEmpty())
			throw new RegistryStorageException("nameSet is empty");
		if (item == null || item.isEmpty())
			throw new RegistryStorageException("item is empty");

		boolean isNotify = channelGroup.bindChannel(item, channel);
		for (String name : nameSet)
		{
			Set<String> set = registryMap.get(name);
			if (set == null)
			{
				set = new HashSet<>();
				registryMap.put(name, set);
			}
			set.add(item);
			if (!isNotify)
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
	public void delete(Set<String> nameSet, String item, Channel channel)
	{
		if (nameSet == null || nameSet.isEmpty())
			throw new RegistryStorageException("nameSet is empty");
		if (item == null || item.isEmpty())
			throw new RegistryStorageException("item is empty");

		boolean isNotify = channelGroup.bindChannel(item, channel);
		for (String name : nameSet)
		{
			Set<String> set = registryMap.get(name);
			if (set != null && !set.isEmpty())
			{
				set.remove(item);
			}
			if (!isNotify)
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
	public void watch(String name, Channel channel)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("name is empty");

		channelGroup.watchChannel(name, channel);
	}

	@Override
	public void unwatch(String name, Channel channel)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("name is empty");

		channelGroup.unwatchChannel(name, channel);
	}

	@Override
	public void disconnect(Channel channel)
	{
		String item = channelGroup.disconnect(channel);
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
