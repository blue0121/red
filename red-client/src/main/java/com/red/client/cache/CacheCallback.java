package com.red.client.cache;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public interface CacheCallback
{

	void onSuccess(CacheInstance data);

	void onFailure(Exception e);

}
