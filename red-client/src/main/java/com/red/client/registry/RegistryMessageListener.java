package com.red.client.registry;

import com.red.client.MessageListener;
import com.red.core.message.Message;
import com.red.core.message.Protocol;
import com.red.core.message.RegistryMessage;
import com.red.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryMessageListener implements MessageListener
{
	private static Logger logger = LoggerFactory.getLogger(RegistryMessageListener.class);

	private RegistryCallback callback;
	private Map<String, Set<RegistryCallback>> callbackMap;

	public RegistryMessageListener()
	{
		this.callbackMap = new ConcurrentHashMap<>();
	}

	public RegistryMessageListener(RegistryCallback callback)
	{
		this.callback = callback;
	}

	public void addRegistryCallback(String name, RegistryCallback callback)
	{
		AssertUtil.notEmpty(name, "name");
		AssertUtil.notNull(callback, "RegistryCall");
		AssertUtil.notNull(callbackMap, "RegistryCallbackMap");

		Set<RegistryCallback> set = callbackMap.putIfAbsent(name, new CopyOnWriteArraySet<>());
		if (set == null)
		{
			set = callbackMap.get(name);
		}
		boolean result = set.add(callback);
		if (result)
		{
			logger.info("Add new RegistryCallback for {}", name);
		}
	}

	public void removeRegistryCallback(String name)
	{
		AssertUtil.notEmpty(name, "name");
		AssertUtil.notNull(callbackMap, "RegistryCallbackMap");

		callbackMap.remove(name);
	}

	@Override
	public void complete(Message message)
	{
		if (message.getProtocol() != Protocol.REGISTRY)
			return;

		RegistryMessage registryMessage = (RegistryMessage) message;
		RegistryInstance instance = RegistryInstance.from(registryMessage);
		if (callback != null)
		{
			callback.onReceive(instance);
		}
		else
		{
			Set<RegistryCallback> set = callbackMap.get(registryMessage.getName());
			if (set == null || set.isEmpty())
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("There is no RegistryCallback for {}", registryMessage.getName());
				}
				return;
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("Receive registry message, name: {}, host: {}", instance.getNameSet(), instance.getHostSet());
			}
			for (RegistryCallback callback : set)
			{
				callback.onReceive(instance);
			}
		}
	}

}
