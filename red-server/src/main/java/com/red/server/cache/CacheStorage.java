package com.red.server.cache;

import java.util.concurrent.ExecutorService;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public interface CacheStorage
{

	void save(String key, byte[] value, long ttl);

	byte[] get(String key);

	void delete(String key);

	ExecutorService getExecutorService();

}
