package com.red.client.registry;

import com.red.core.message.Message;
import com.red.core.message.Protocol;
import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class FutureRegistryInstance implements Future<RegistryInstance>
{
	private final Future<Message> future;

	public FutureRegistryInstance(Future<Message> future)
	{
		this.future = future;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		throw  new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled()
	{
		throw  new UnsupportedOperationException();
	}

	@Override
	public boolean isDone()
	{
		return future.isDone();
	}

	@Override
	public RegistryInstance get() throws InterruptedException, ExecutionException
	{
		Message message = future.get();
		RuntimeException exception = this.toException(message);
		if (exception != null)
			throw exception;

		return this.toInstance(message);
	}

	@Override
	public RegistryInstance get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		Message message = future.get(timeout, unit);
		RuntimeException exception = this.toException(message);
		if (exception != null)
			throw exception;

		return this.toInstance(message);
	}

	public RuntimeException getException() throws InterruptedException, ExecutionException
	{
		Message message = future.get();
		return this.toException(message);
	}

	public RuntimeException getException(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		Message message = future.get(timeout, unit);
		return this.toException(message);
	}

	private RegistryInstance toInstance(Message message)
	{
		if (message.getProtocol() != Protocol.REGISTRY)
			return null;

		RegistryMessage registryMessage = (RegistryMessage) message;
		return RegistryInstance.from(registryMessage);
	}

	private RuntimeException toException(Message message)
	{
		if (message.getProtocol() != Protocol.REGISTRY)
			return null;

		RegistryMessage registryMessage = (RegistryMessage) message;
		RuntimeException exception = null;
		if (registryMessage.getCode() == ResponseCode.ERROR)
		{
			exception = new RuntimeException(registryMessage.getMessage());
		}
		else if (registryMessage.getCode() == ResponseCode.REGISTRY)
		{
			exception = new RegistryClientException(registryMessage.getMessage());
		}

		return exception;
	}

}
