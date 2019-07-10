package com.red.server.common;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-08
 */
public class RedServerException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public RedServerException(String message)
	{
		super(message);
	}

	public RedServerException(Throwable throwable)
	{
		super(throwable);
	}

}
