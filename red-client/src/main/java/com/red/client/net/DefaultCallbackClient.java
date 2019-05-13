package com.red.client.net;

import com.red.client.CallbackClient;
import com.red.client.registry.RegistryCallback;
import com.red.client.registry.RegistryInstance;
import com.red.core.message.Message;
import com.red.core.message.Protocol;
import com.red.core.message.RegistryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class DefaultCallbackClient implements CallbackClient
{
	private static Logger logger = LoggerFactory.getLogger(DefaultCallbackClient.class);

	private RegistryCallback registryCallback;

	public DefaultCallbackClient()
	{
	}

	@Override
	public void complete(Message message)
	{
		if (message.getProtocol() == Protocol.REGISTRY)
		{
			RegistryInstance instance = RegistryInstance.from((RegistryMessage) message);
			registryCallback.onReceive(instance);
			if (logger.isDebugEnabled())
			{
				logger.debug("Receive registry message, prefix: {}, name: {}, host: {}", instance.getPrefix(), instance.getName(), instance.getHostList());
			}
		}
	}

	public void setRegistryCallback(RegistryCallback registryCallback)
	{
		this.registryCallback = registryCallback;
	}
}
