package com.red.server.registry;

import com.red.server.common.RedServerException;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class RegistryStorageException extends RedServerException
{
	private static final long serialVersionUID = 1L;

	public RegistryStorageException(String message)
	{
		super(message);
	}

	public RegistryStorageException(Throwable throwable)
	{
		super(throwable);
	}

}
