package com.red.client.net;

import com.red.client.HandlerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-08
 */
public class HandlerClientCache
{
	private static Logger logger = LoggerFactory.getLogger(HandlerClientCache.class);

	private final List<HandlerClientItem> handlerList;
	private final Random random;
	private final Lock readLock;
	private final Lock writeLock;
	private int size;

	public HandlerClientCache()
	{
		this.random = new Random();
		this.handlerList = new LinkedList<>();
		this.size = 0;
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		readLock = readWriteLock.readLock();
		writeLock = readWriteLock.writeLock();
	}

	public void addHandlerClient(String key, HandlerClient handler)
	{
		writeLock.lock();
		try
		{
			handlerList.add(new HandlerClientItem(key, handler));
			size++;
		}
		finally
		{
			writeLock.unlock();
		}
	}

	public void removeHandlerClient(String key)
	{
		if (key == null || key.isEmpty())
			return;

		writeLock.lock();
		try
		{
			Iterator<HandlerClientItem> iterator = handlerList.iterator();
			while (iterator.hasNext())
			{
				HandlerClientItem item = iterator.next();
				if (item.getKey().equals(key))
				{
					iterator.remove();
					size--;
					break;
				}
			}
		}
		finally
		{
			writeLock.unlock();
		}
	}

	public HandlerClient getHandlerClient()
	{
		if (size == 0)
			return null;

		readLock.lock();
		try
		{
			if (size == 1)
				return handlerList.get(0).getHandler();

			int index = random.nextInt(size);
			return handlerList.get(index).getHandler();
		}
		finally
		{
			readLock.unlock();
		}
	}

	public List<HandlerClient> listHandlerClient()
	{
		List<HandlerClient> list = new ArrayList<>();
		if (size == 0)
			return list;

		readLock.lock();
		try
		{
			for (HandlerClientItem item : handlerList)
			{
				list.add(item.getHandler());
			}
			return list;
		}
		finally
		{
			readLock.unlock();
		}
	}

}
