package com.red.server.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.red.core.util.AssertUtil;
import com.red.server.config.RedConfig;
import com.red.server.config.RedConfigItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class MemoryCacheStorage implements CacheStorage
{
	private static Logger logger = LoggerFactory.getLogger(MemoryCacheStorage.class);

	private final Cache<String, CacheObject> cache;
	private final CacheObjectExpiry expiry;
	private final RedConfig config;

	public MemoryCacheStorage()
	{
		this.config = RedConfig.getInstance();
		this.expiry = new CacheObjectExpiry();
		this.cache = Caffeine.newBuilder()
				.expireAfter(expiry)
				.maximumSize(config.getInt(RedConfigItem.CACHE_MEMORY_MAX_SIZE, RedConfigItem.CACHE_MEMORY_MAX_SIZE_VALUE))
				.build();
	}

	@Override
	public void set(String key, CacheObject object)
	{
		AssertUtil.notEmpty(key, "Key");
		if (object != null)
		{
			cache.put(key, object);
		}
		logger.debug("Set cache message, key: {}", key);
	}

	@Override
	public CacheObject get(String key)
	{
		AssertUtil.notEmpty(key, "Key");
		CacheObject cacheObject = cache.getIfPresent(key);
		logger.debug("Get cache message, key: {}", key);
		return cacheObject;
	}

	@Override
	public void delete(String key)
	{
		AssertUtil.notEmpty(key, "Key");
		cache.invalidate(key);
		logger.debug("Delete cache message, key: {}", key);
	}

}
