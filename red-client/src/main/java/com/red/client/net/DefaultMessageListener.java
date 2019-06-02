package com.red.client.net;

import com.red.client.MessageListener;
import com.red.core.message.Message;
import com.red.core.message.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class DefaultMessageListener implements MessageListener
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMessageListener.class);

	private MessageListener registryCallback;

	public DefaultMessageListener()
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

	public void setRegistryCallback(MessageListener registryCallback)
	{
		this.registryCallback = registryCallback;
	}
}
