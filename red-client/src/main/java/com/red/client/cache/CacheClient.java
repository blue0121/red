package com.red.client.cache;

import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public interface CacheClient
{

	void setSync(CacheInstance instance);

	void setAsync(CacheInstance instance, CacheCallback callback);

	CacheInstance getSync(String key);

	Future<CacheInstance> getAsync(String key, CacheCallback callback);

	void deleteSync(String key);

	void deleteAsync(String key, CacheCallback callback);

}
