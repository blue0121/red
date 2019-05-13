package com.red.client.registry;

import com.red.client.HandlerClient;
import com.red.core.message.Message;
import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;

import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class DefaultRegistryClient implements RegistryClient
{
	private final HandlerClient handlerClient;

	public DefaultRegistryClient(HandlerClient handlerClient)
	{
		this.handlerClient = handlerClient;
	}

	@Override
	public RegistryInstance saveSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.SAVE, instance);
	}

	@Override
	public Future<RegistryInstance> saveAsync(RegistryInstance instance, RegistryCallback callback)
	{
		return this.invokeAsync(RegistryCommand.SAVE, instance, callback);
	}

	@Override
	public RegistryInstance deleteSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.DELETE, instance);
	}

	@Override
	public Future<RegistryInstance> deleteAsync(RegistryInstance instance, RegistryCallback callback)
	{
		return this.invokeAsync(RegistryCommand.DELETE, instance, callback);
	}

	@Override
	public RegistryInstance listSync(RegistryInstance instance)
	{
		return this.invokeSync(RegistryCommand.LIST, instance);
	}

	@Override
	public Future<RegistryInstance> listAsync(RegistryInstance instance, RegistryCallback callback)
	{
		return this.invokeAsync(RegistryCommand.LIST, instance, callback);
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
		RegistryMessage message = instance.build(command);
		RegistryCallbackClient callbackClient = null;
		if (callback != null)
		{
			callbackClient = new RegistryCallbackClient(callback);
		}
		Future<Message> future = handlerClient.sendMessage(message, callbackClient);
		FutureRegistryInstance futureRegistryInstance = new FutureRegistryInstance(future);
		return futureRegistryInstance;
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
