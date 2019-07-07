package com.red.server.cache;

import com.red.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class MemoryCacheStorage implements CacheStorage
{
	private static Logger logger = LoggerFactory.getLogger(MemoryCacheStorage.class);

	private final ExecutorService executorService;

	public MemoryCacheStorage()
	{
		this.executorService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void save(String key, byte[] value)
	{
		AssertUtil.notEmpty(key, "Key");

		logger.debug("Save cache message, key: {}", key);
	}

	@Override
	public byte[] get(String key)
	{
		AssertUtil.notEmpty(key, "Key");

		logger.debug("Get cache message, key: {}", key);
		return new byte[0];
	}

	@Override
	public void delete(String key)
	{
		AssertUtil.notEmpty(key, "Key");

		logger.debug("Delete cache message, key: {}", key);
	}

	@Override
	public ExecutorService getExecutorService()
	{
		return executorService;
	}
}
