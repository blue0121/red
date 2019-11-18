package blue.red.client.registry;

import blue.red.client.HandlerClient;
import blue.red.client.RedClientException;
import blue.red.client.RedFuture;
import blue.red.core.message.Message;
import blue.red.core.message.RegistryCommand;
import blue.red.core.message.RegistryMessage;
import blue.red.core.util.AssertUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class DefaultRegistryClient implements RegistryClient
{
	private final HandlerClient handlerClient;
	private final RegistryMessageListener callbackClient;

	public DefaultRegistryClient(HandlerClient handlerClient)
	{
		this.handlerClient = handlerClient;
		this.callbackClient = new RegistryMessageListener();
		this.handlerClient.setRegistryListener(callbackClient);
	}

	@Override
	public RegistryInstance saveSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.SAVE, instance);
	}

	@Override
	public RedFuture<RegistryInstance> saveAsync(RegistryInstance instance, RegistryCallback callback)
	{
		return this.invokeAsync(RegistryCommand.SAVE, instance, callback);
	}

	@Override
	public void saveAtRate(RegistryInstance instance, long period, TimeUnit unit)
	{
		AssertUtil.notNull(instance, "RegistryInstance");
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		handlerClient.sendMessageAtFixRate(message, period, unit);
	}

	@Override
	public RegistryInstance deleteSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.DELETE, instance);
	}

	@Override
	public RedFuture<RegistryInstance> deleteAsync(RegistryInstance instance, RegistryCallback callback)
	{
		return this.invokeAsync(RegistryCommand.DELETE, instance, callback);
	}

	@Override
	public RegistryInstance listSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.LIST, instance);
	}

	@Override
	public RedFuture<RegistryInstance> listAsync(RegistryInstance instance, RegistryCallback callback)
	{
		return this.invokeAsync(RegistryCommand.LIST, instance, callback);
	}

	@Override
	public void bind(RegistryInstance instance)
	{
		this.invokeSync(RegistryCommand.BIND, instance);
	}

	@Override
	public void unbind(RegistryInstance instance)
	{
		this.invokeSync(RegistryCommand.UNBIND, instance);
	}

	@Override
	public void watch(RegistryInstance instance, RegistryCallback callback)
	{
		this.invokeAsync(RegistryCommand.WATCH, instance, callback);
	}

	@Override
	public void unwatch(RegistryInstance instance)
	{
		this.invokeSync(RegistryCommand.UNWATCH, instance);
	}

	private FutureRegistryInstance invokeAsync(RegistryCommand command, RegistryInstance instance, RegistryCallback callback)
	{
		this.check(command, instance);
		RegistryMessage message = instance.build(command);
		RegistryMessageListener messageListener = null;
		if (callback != null)
		{
			messageListener = new RegistryMessageListener(callback);
		}
		this.registryListener(message, callback);
		RedFuture<Message> future = handlerClient.sendMessage(message, messageListener);
		FutureRegistryInstance futureRegistryInstance = new FutureRegistryInstance(future);
		return futureRegistryInstance;
	}

	private void check(RegistryCommand command, RegistryInstance instance)
	{
		AssertUtil.notNull(instance, "RegistryInstance");

		if (command == RegistryCommand.BIND || command == RegistryCommand.UNBIND)
		{
			if (instance.getHostSet().isEmpty())
				throw new RegistryClientException("host is empty");
		}
		else if (command == RegistryCommand.LIST)
		{
			if (instance.getNameSet().size() != 1)
				throw new RegistryClientException("name size must be 1");
		}
		else
		{
			if (instance.getNameSet().isEmpty())
				throw new RegistryClientException("name is empty");
		}
	}

	private void registryListener(RegistryMessage message, RegistryCallback callback)
	{
		if (message.getCommand() == RegistryCommand.WATCH)
		{
			for (String name : message.getNameSet())
			{
				callbackClient.addRegistryCallback(name, callback);
			}
		}
		else if (message.getCommand() == RegistryCommand.UNWATCH)
		{
			for (String name : message.getNameSet())
			{
				callbackClient.removeRegistryCallback(name);
			}
		}
	}

	private RegistryInstance invokeSync(RegistryCommand command, RegistryInstance instance)
	{
		FutureRegistryInstance futureRegistryInstance = this.invokeAsync(command, instance, null);
		try
		{
			return futureRegistryInstance.get();
		}
		catch (Exception e)
		{
			if (e instanceof RedClientException)
				throw (RedClientException)e;

			throw new RegistryClientException(e);
		}
	}

}
