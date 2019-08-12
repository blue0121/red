package com.red.client.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryListenerTest implements RegistryCallback
{
	private static Logger logger = LoggerFactory.getLogger(RegistryListenerTest.class);

	public RegistryListenerTest()
	{
	}

	@Override
	public void onSuccess(RegistryInstance data)
	{
		logger.info("Receive registry message, name: {}, host: {}", data.getNameSet(), data.getHostSet());
	}

	@Override
	public void onFailure(Exception e)
	{
		logger.error("Receive registry message error, ", e);
	}
}
