package com.red.server.cache;

import com.github.benmanes.caffeine.cache.Expiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-08
 */
public class CacheObjectExpiry implements Expiry<String, CacheObject>
{
	private static Logger logger = LoggerFactory.getLogger(CacheObjectExpiry.class);
	private static final long NANO = 1_000_000;

	public CacheObjectExpiry()
	{
	}

	@Override
	public long expireAfterCreate(String key, CacheObject value, long currentTime)
	{
		long duration = this.getDuration(value);
		logger.debug("create, key: {}, ttl: {}ms", key, value.getTtl());
		return duration;
	}

	@Override
	public long expireAfterUpdate(String key, CacheObject value, long currentTime, long currentDuration)
	{
		long duration = this.getDuration(value);
		logger.debug("update, key: {}, ttl: {}ms", key, value.getTtl());
		return duration;
	}

	private long getDuration(CacheObject value)
	{
		long duration = Long.MAX_VALUE;
		if (value != null && value.getTtl() > 0)
		{
			duration = value.getTtl() * NANO;
		}
		return duration;
	}

	@Override
	public long expireAfterRead(String key, CacheObject value, long currentTime, long currentDuration)
	{
		return currentDuration;
	}
}
