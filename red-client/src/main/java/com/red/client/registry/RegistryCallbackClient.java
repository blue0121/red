package com.red.client.registry;

import com.red.client.CallbackClient;
import com.red.core.message.Message;
import com.red.core.message.Protocol;
import com.red.core.message.RegistryMessage;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryCallbackClient implements CallbackClient
{
	private final RegistryCallback callback;

	public RegistryCallbackClient(RegistryCallback callback)
	{
		this.callback = callback;
	}

	@Override
	public void complete(Message message)
	{
		if (callback == null)
			return;

		if (message.getProtocol() != Protocol.REGISTRY)
			return;

		RegistryMessage registryMessage = (RegistryMessage) message;
		RegistryInstance instance = RegistryInstance.from(registryMessage);
		callback.onReceive(instance);
	}
}
