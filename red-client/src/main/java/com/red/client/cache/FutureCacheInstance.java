package com.red.client.cache;

import com.red.client.RedClientException;
import com.red.client.RedFuture;
import com.red.core.message.CacheMessage;
import com.red.core.message.Message;
import com.red.core.message.Protocol;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class FutureCacheInstance implements RedFuture<CacheInstance>
{
	private final RedFuture<Message> future;

	public FutureCacheInstance(RedFuture<Message> future)
	{
		this.future = future;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDone()
	{
		return future.isDone();
	}

	@Override
	public CacheInstance get() throws InterruptedException, ExecutionException
	{
		Message message = future.get();
		return this.toInstance(message);
	}

	@Override
	public CacheInstance get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		Message message = future.get(timeout, unit);
		return this.toInstance(message);
	}

	public RedClientException getException()
	{
		return future.getException();
	}

	private CacheInstance toInstance(Message message)
	{
		if (message.getProtocol() != Protocol.CACHE)
			return null;

		CacheMessage cacheMessage = (CacheMessage) message;
		return CacheInstance.from(cacheMessage);
	}

}
