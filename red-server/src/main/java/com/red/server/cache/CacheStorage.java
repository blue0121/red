package com.red.server.cache;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public interface CacheStorage
{

	void set(String key, CacheObject object);

	CacheObject get(String key);

	void delete(String key);

}
