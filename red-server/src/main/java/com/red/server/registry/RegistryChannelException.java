package com.red.server.registry;

import com.red.server.common.RedServerException;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class RegistryChannelException extends RedServerException
{
	private static final long serialVersionUID = 1L;

	public RegistryChannelException(String message)
	{
		super(message);
	}

	public RegistryChannelException(Throwable throwable)
	{
		super(throwable);
	}

}
