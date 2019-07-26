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

	private RegistryListener listener;
	private Map<String, Set<RegistryListener>> listenerMap;

	public RegistryMessageListener()
	{
		this(null);
	}

	public RegistryMessageListener(RegistryListener listener)
	{
		this.listener = listener;
		this.listenerMap = new ConcurrentHashMap<>();
	}

	public void addRegistryCallback(String name, RegistryListener listener)
	{
		AssertUtil.notEmpty(name, "name");
		AssertUtil.notNull(listener, "RegistryListener");

		Set<RegistryListener> set = listenerMap.putIfAbsent(name, new CopyOnWriteArraySet<>());
		if (set == null)
		{
			set = listenerMap.get(name);
		}
		boolean result = set.add(listener);
		if (result)
		{
			logger.info("Add new RegistryListener for {}", name);
		}
	}

	public void removeRegistryCallback(String name)
	{
		AssertUtil.notEmpty(name, "name");

		listenerMap.remove(name);
	}

	@Override
	public void complete(Message message)
	{
		if (message.getProtocol() != Protocol.REGISTRY)
			return;

		RegistryMessage registryMessage = (RegistryMessage) message;
		RegistryInstance instance = RegistryInstance.from(registryMessage);
		if (listener != null)
		{
			listener.onReceive(instance);
		}
		else
		{
			Set<RegistryListener> set = null;
			if (registryMessage.getName() != null)
			{
				set = listenerMap.get(registryMessage.getName());
			}
			if (set == null || set.isEmpty())
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("There is no RegistryListener for {}", registryMessage.getName());
				}
				return;
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("Receive registry message, name: {}, host: {}", instance.getNameSet(), instance.getHostSet());
			}
			for (RegistryListener listener : set)
			{
				listener.onReceive(instance);
			}
		}
	}

}
