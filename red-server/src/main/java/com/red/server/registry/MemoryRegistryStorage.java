package com.red.server.registry;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class MemoryRegistryStorage implements RegistryStorage
{
	private static Logger logger = LoggerFactory.getLogger(MemoryRegistryStorage.class);

	private final Map<String, Set<String>> registryMap;
	private final Map<String, ReadWriteLock> lockMap;

	public MemoryRegistryStorage()
	{
		this.registryMap = new HashMap<>();
		this.lockMap = new HashMap<>();
	}

	@Override
	public void save(String name, Set<String> itemSet)
	{
		if (name == null || name.isEmpty() || itemSet == null || itemSet.isEmpty())
		{
			logger.warn("Name or itemList is empty");
			return;
		}

		ReadWriteLock lock = this.getReadWriteLock(name);
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try
		{
			Set<String> set = registryMap.get(name);
			if (set == null)
			{
				set = new HashSet<>();
				registryMap.put(name, set);
			}
			set.addAll(itemSet);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	@Override
	public void delete(String name, String item)
	{
		if (name == null || name.isEmpty() || item == null || item.isEmpty())
		{
			logger.warn("Name or item is empty");
			return;
		}

		ReadWriteLock lock = this.getReadWriteLock(name);
		Lock writeLock = lock.writeLock();
		writeLock.lock();
		try
		{
			Set<String> set = registryMap.get(name);
			if (set != null && !set.isEmpty())
			{
				set.remove(item);
			}
		}
		finally
		{
			writeLock.unlock();
		}
	}

	@Override
	public Set<String> list(String name)
	{
		Set<String> itemSet = new HashSet<>();
		if (name == null || name.isEmpty())
		{
			logger.warn("Name is empty");
			return itemSet;
		}

		ReadWriteLock lock = this.getReadWriteLock(name);
		Lock readLock = lock.readLock();
		readLock.lock();
		try
		{
			Set<String> set = registryMap.get(name);
			if (set != null && !set.isEmpty())
			{
				itemSet.addAll(set);
			}
		}
		finally
		{
			readLock.unlock();
		}
		return itemSet;
	}

	@Override
	public void watch(String name, Channel channel)
	{

	}

	private ReadWriteLock getReadWriteLock(String name)
	{
		ReadWriteLock lock = lockMap.get(name);
		if (lock == null)
		{
			synchronized (this)
			{
				lock = lockMap.get(name);
				if (lock == null)
				{
					lock = new ReentrantReadWriteLock();
					lockMap.put(name, lock);
				}
			}
		}
		return lock;
	}

}
