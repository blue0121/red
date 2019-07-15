package com.red.client.net;

import com.red.client.Future;
import com.red.client.MessageListener;
import com.red.client.RedClientException;
import com.red.client.registry.RegistryClientException;
import com.red.core.message.Message;
import com.red.core.message.Response;
import com.red.core.message.ResponseCode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public class FutureClient implements Future
{
	private CountDownLatch latch;
	private MessageListener listener;

	private Message request;
	private Message response;
	private long start;

	public FutureClient(Message request)
	{
		this(request, null);
	}

	public FutureClient(Message request, MessageListener listener)
	{
		this.request = request;
		this.listener = listener;

		this.latch = new CountDownLatch(1);
		this.start = System.currentTimeMillis();
	}

	public void done(Message response)
	{
		this.response = response;
		this.latch.countDown();

		if (listener != null)
		{
			listener.complete(response);
		}
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
		return response != null;
	}

	@Override
	public Message get() throws InterruptedException, ExecutionException
	{
		latch.await();

		RedClientException exception = this.getException();
		if (exception != null)
			throw exception;

		return response;
	}

	@Override
	public Message get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		boolean success = latch.await(timeout, unit);
		if (!success)
			throw new TimeoutException("Read timeout!");

		RedClientException exception = this.getException();
		if (exception != null)
			throw exception;

		return response;
	}

	public Message getRequest()
	{
		return request;
	}

	public long getStart()
	{
		return start;
	}

	@Override
	public RedClientException getException()
	{
		if (!(response instanceof Response))
			return null;

		RedClientException exception = null;
		Response resp = (Response) response;
		if (resp.getCode() == ResponseCode.ERROR)
		{
			exception = new RedClientException(resp.getMessage());
		}
		else if (resp.getCode() == ResponseCode.REGISTRY)
		{
			exception = new RegistryClientException(resp.getMessage());
		}
		return exception;
	}
}
