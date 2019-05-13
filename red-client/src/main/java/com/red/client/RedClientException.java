package com.red.client;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class RedClientException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public RedClientException(String message)
	{
		super(message);
	}

	public RedClientException(Throwable cause)
	{
		super(cause);
	}
}
