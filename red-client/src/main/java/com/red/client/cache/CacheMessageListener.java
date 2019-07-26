package com.red.client.cache;

import com.red.client.MessageListener;
import com.red.client.RedClientException;
import com.red.client.net.FutureClient;
import com.red.core.message.CacheMessage;
import com.red.core.message.Message;
import com.red.core.message.Protocol;
import com.red.core.message.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public class CacheMessageListener implements MessageListener
{
	private static Logger logger = LoggerFactory.getLogger(CacheMessageListener.class);

	private CacheCallback callback;

	public CacheMessageListener()
	{
	}

	public CacheMessageListener(CacheCallback callback)
	{
		this.callback = callback;
	}

	@Override
	public void complete(Message message)
	{
		if (message.getProtocol() != Protocol.CACHE)
			return;

		CacheMessage cacheMessage = (CacheMessage) message;
		if (callback != null)
		{
			if (cacheMessage.getCode() == ResponseCode.SUCCESS)
			{
				CacheInstance instance = CacheInstance.from(cacheMessage);
				callback.onSuccess(instance);
			}
			else
			{
				RedClientException exception = FutureClient.getException(cacheMessage);
				callback.onFailure(exception);
			}
		}
	}
}
