package blue.red.client.net;

import blue.red.client.MessageListener;
import blue.red.client.RedClientException;
import blue.red.client.RedFuture;
import blue.red.client.cache.CacheClientException;
import blue.red.client.registry.RegistryClientException;
import blue.red.core.message.Message;
import blue.red.core.message.Response;
import blue.red.core.message.ResponseCode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public class FutureClient implements RedFuture<Message>
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

		return getException((Response) response);
	}

	public static RedClientException getException(Response response)
	{
		RedClientException exception = null;
		if (response.getCode() == ResponseCode.ERROR)
		{
			exception = new RedClientException(response.getMessage());
		}
		else if (response.getCode() == ResponseCode.REGISTRY)
		{
			exception = new RegistryClientException(response.getMessage());
		}
		else if (response.getCode() == ResponseCode.CACHE)
		{
			exception = new CacheClientException(response.getMessage());
		}
		return exception;
	}


}
