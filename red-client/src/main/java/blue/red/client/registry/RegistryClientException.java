package blue.red.client.registry;

import blue.red.client.RedClientException;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryClientException extends RedClientException
{
	public RegistryClientException(String message)
	{
		super(message);
	}

	public RegistryClientException(Throwable cause)
	{
		super(cause);
	}
}
