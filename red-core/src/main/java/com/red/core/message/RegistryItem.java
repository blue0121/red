package com.red.core.message;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 1.0 2019-09-16
 */
public class RegistryItem
{
	private String item;
	private String token;

	public RegistryItem()
	{
	}

	public RegistryItem(String item)
	{
		this.item = item;
	}

	public RegistryItem(String item, String token)
	{
		this.item = item;
		this.token = token;
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
		RegistryItem that = (RegistryItem) o;
		return item.equals(that.item);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item);
	}

	@Override
	public String toString()
	{
		return String.format("RegistryItem{item: %s, token: %s}", item, token);
	}

	public String getItem()
	{
		return item;
	}

	public void setItem(String item)
	{
		this.item = item;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
