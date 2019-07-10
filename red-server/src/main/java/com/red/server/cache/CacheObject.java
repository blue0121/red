package com.red.server.cache;

import com.red.core.message.CacheMessage;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-08
 */
public class CacheObject
{
	private byte[] value;
	private long ttl;

	public CacheObject()
	{
	}

	public CacheObject(byte[] value, long ttl)
	{
		this.value = value;
		this.ttl = ttl;
	}

	public static CacheObject from(CacheMessage message)
	{
		CacheObject object = new CacheObject();
		object.setValue(message.getValue());
		object.setTtl(message.getTtl());
		return object;
	}

	public byte[] getValue()
	{
		return value;
	}

	public void setValue(byte[] value)
	{
		this.value = value;
	}

	public long getTtl()
	{
		return ttl;
	}

	public void setTtl(long ttl)
	{
		this.ttl = ttl;
	}
}
