package com.red.server.registry;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
	public void save(String name, Collection<String> itemList)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("Name must be not empty");
		if (itemList == null || itemList.isEmpty())
			throw new RegistryStorageException("Item list must be not empty");

		Set<String> set = registryMap.get(name);
		if (set == null)
		{
			set = new HashSet<>();
			registryMap.put(name, set);
		}
		set.addAll(itemList);
		channelGroup.notify(name, set);
		if (logger.isDebugEnabled())
		{
			logger.debug("Save registry message, name: {}, itemList: {}", name, itemList);
		}
	}

	@Override
	public void delete(String name, String item)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("Name must be not empty");
		if (item == null || item.isEmpty())
			throw new RegistryStorageException("Item must be not empty");

		Set<String> set = registryMap.get(name);
		if (set != null && !set.isEmpty())
		{
			set.remove(item);
		}
		channelGroup.notify(name, set);
		if (logger.isDebugEnabled())
		{
			logger.debug("Delete registry message, name: {}, item: {}", name, item);
		}
	}

	@Override
	public Set<String> list(String name)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("Name must be not empty");

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
			throw new RegistryStorageException("Name must be not empty");

		channelGroup.addChannel(name, channel);
	}

	@Override
	public void unwatch(String name, Channel channel)
	{
		if (name == null || name.isEmpty())
			throw new RegistryStorageException("Name must be not empty");

		channelGroup.removeChannel(name, channel);
	}

	@Override
	public ExecutorService getExecutorService()
	{
		return executorService;
	}


}
