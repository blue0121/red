package com.red.client.cache;

import com.red.client.RedClientException;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class CacheClientException extends RedClientException
{
	public CacheClientException(String message)
	{
		super(message);
	}

	public CacheClientException(Throwable cause)
	{
		super(cause);
	}
}
