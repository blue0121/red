package com.red.client.net;

import com.red.client.HandlerClient;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-08
 */
public class HandlerClientItem
{
	private String key;
	private HandlerClient handler;

	public HandlerClientItem(String key, HandlerClient handler)
	{
		this.key = key;
		this.handler = handler;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		HandlerClientItem that = (HandlerClientItem) o;
		return key.equals(that.key);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(key);
	}

	public String getKey()
	{
		return key;
	}

	public HandlerClient getHandler()
	{
		return handler;
	}
}
