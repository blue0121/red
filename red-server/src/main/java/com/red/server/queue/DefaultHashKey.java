package com.red.server.queue;

/**
 * @author Jin Zheng
 * @since 2019-07-20
 */
public class DefaultHashKey implements HashKey
{
	public DefaultHashKey()
	{
	}

	@Override
	public int calculate(String key, int length)
	{
		if (length <= 1)
			return 0;

		return key.hashCode() % length;
	}

}
