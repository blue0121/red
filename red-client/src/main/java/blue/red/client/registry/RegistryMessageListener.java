package blue.red.client.registry;

import blue.red.client.MessageListener;
import blue.red.client.RedClientException;
import blue.red.client.net.FutureClient;
import blue.red.core.message.Message;
import blue.red.core.message.Protocol;
import blue.red.core.message.RegistryMessage;
import blue.red.core.message.ResponseCode;
import blue.red.core.util.AssertUtil;
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
	private Map<String, Set<RegistryCallback>> listenerMap;

	public RegistryMessageListener()
	{
		this(null);
	}

	public RegistryMessageListener(RegistryCallback callback)
	{
		this.callback = callback;
		this.listenerMap = new ConcurrentHashMap<>();
	}

	public void addRegistryCallback(String name, RegistryCallback listener)
	{
		AssertUtil.notEmpty(name, "name");
		AssertUtil.notNull(listener, "RegistryListener");

		Set<RegistryCallback> set = listenerMap.computeIfAbsent(name, k -> new CopyOnWriteArraySet<>());
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
		RegistryInstance instance = null;
		RedClientException exception = null;
		if (registryMessage.getCode() == ResponseCode.SUCCESS)
		{
			instance = RegistryInstance.from(registryMessage);
		}
		else
		{
			exception = FutureClient.getException(registryMessage);
		}
		if (callback != null)
		{
			this.invoke(callback, instance, exception);
		}
		else
		{
			Set<RegistryCallback> set = null;
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
			for (RegistryCallback listener : set)
			{
				this.invoke(listener, instance, exception);
			}
		}
	}

	private void invoke(RegistryCallback callback, RegistryInstance instance, RedClientException exception)
	{
		if (instance != null)
		{
			callback.onSuccess(instance);
		}
		else if (exception != null)
		{
			callback.onFailure(exception);
		}
	}

}
