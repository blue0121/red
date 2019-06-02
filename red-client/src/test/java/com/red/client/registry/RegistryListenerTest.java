package com.red.client.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryListenerTest implements RegistryListener
{
	private static Logger logger = LoggerFactory.getLogger(RegistryListenerTest.class);

	public RegistryListenerTest()
	{
	}

	@Override
	public void onReceive(RegistryInstance instance)
	{
		logger.info("Receive registry message, name: {}, host: {}", instance.getNameSet(), instance.getHostSet());
	}
}
