package com.red.client.net;

import com.red.client.CallbackClient;
import com.red.core.message.Message;
import com.red.core.message.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class DefaultCallbackClient implements CallbackClient
{
	private static Logger logger = LoggerFactory.getLogger(DefaultCallbackClient.class);

	private CallbackClient registryCallback;

	public DefaultCallbackClient()
	{
	}

	@Override
	public void complete(Message message)
	{
		if (message.getProtocol() == Protocol.REGISTRY && registryCallback != null)
		{
			registryCallback.complete(message);
		}
	}

	public void setRegistryCallback(CallbackClient registryCallback)
	{
		this.registryCallback = registryCallback;
	}
}
