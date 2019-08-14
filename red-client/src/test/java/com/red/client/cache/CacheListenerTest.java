package com.red.client.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class CacheListenerTest implements CacheCallback
{
	private static Logger logger = LoggerFactory.getLogger(CacheListenerTest.class);

	public CacheListenerTest()
	{
	}

	@Override
	public void onSuccess(CacheInstance data)
	{
		logger.info("Receive cache message, key: {}", data.getKey());
	}

	@Override
	public void onFailure(Exception e)
	{
		logger.error("Receive cache message error, ", e);
	}
}
