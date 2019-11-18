package blue.red.server.cache;

import blue.red.server.common.RedServerException;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class CacheStorageException extends RedServerException
{
	private static final long serialVersionUID = 1L;

	public CacheStorageException(String message)
	{
		super(message);
	}

	public CacheStorageException(Throwable throwable)
	{
		super(throwable);
	}

}
