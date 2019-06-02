package com.red.client.registry;

import com.red.client.HandlerClient;
import com.red.core.message.Message;
import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import com.red.core.util.AssertUtil;

import java.util.concurrent.Future;

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
	public Future<RegistryInstance> saveAsync(RegistryInstance instance, RegistryListener callback)
	{
		return this.invokeAsync(RegistryCommand.SAVE, instance, callback);
	}

	@Override
	public RegistryInstance deleteSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.DELETE, instance);
	}

	@Override
	public Future<RegistryInstance> deleteAsync(RegistryInstance instance, RegistryListener callback)
	{
		return this.invokeAsync(RegistryCommand.DELETE, instance, callback);
	}

	@Override
	public RegistryInstance listSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.LIST, instance);
	}

	@Override
	public Future<RegistryInstance> listAsync(RegistryInstance instance, RegistryListener callback)
	{
		return this.invokeAsync(RegistryCommand.LIST, instance, callback);
	}

	@Override
	public void watch(RegistryInstance instance, RegistryListener callback)
	{
		this.invokeAsync(RegistryCommand.WATCH, instance, callback);
	}

	@Override
	public void unwatch(RegistryInstance instance)
	{
		this.invokeSync(RegistryCommand.UNWATCH, instance);
	}

	private FutureRegistryInstance invokeAsync(RegistryCommand command, RegistryInstance instance, RegistryListener listener)
	{
		AssertUtil.notNull(command, "RegistryCommand");
		AssertUtil.notNull(instance, "RegistryInstance");
		RegistryMessage message = instance.build(command);
		RegistryMessageListener messageListener = null;
		if (listener != null)
		{
			messageListener = new RegistryMessageListener(listener);
		}
		this.registryListener(message, listener);
		Future<Message> future = handlerClient.sendMessage(message, messageListener);
		FutureRegistryInstance futureRegistryInstance = new FutureRegistryInstance(future);
		return futureRegistryInstance;
	}

	private void registryListener(RegistryMessage message, RegistryListener listener)
	{
		if (message.getCommand() == RegistryCommand.WATCH)
		{
			callbackClient.addRegistryCallback(message.getName(), listener);
		}
		else if (message.getCommand() == RegistryCommand.UNWATCH)
		{
			callbackClient.removeRegistryCallback(message.getName());
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
			throw new RegistryClientException(e);
		}
	}

}
