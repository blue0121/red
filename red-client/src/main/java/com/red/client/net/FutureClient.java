package com.red.client.net;

import com.red.client.CallbackClient;
import com.red.core.message.Message;

import java.util.concurrent.*;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public class FutureClient implements Future<Message>
{
	private CountDownLatch latch;
	private CallbackClient callback;

	private Message request;
	private Message response;
	private long start;

	public FutureClient(Message request)
	{
		this(request, null);
	}

	public FutureClient(Message request, CallbackClient callback)
	{
		this.request = request;
		this.callback = callback;

		this.latch = new CountDownLatch(1);
		this.start = System.currentTimeMillis();
	}

	public void done(Message response)
	{
		this.response = response;
		this.latch.countDown();

		if (callback != null)
		{
			callback.complete(response);
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

		return response;
	}

	@Override
	public Message get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		boolean success = latch.await(timeout, unit);
		if (!success)
			throw new TimeoutException("Read timeout!");

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
}
