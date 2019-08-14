package com.red.client.cache;

import com.red.core.message.CacheCommand;
import com.red.core.message.CacheMessage;
import com.red.core.util.CompressUtil;
import com.red.core.util.Constant;
import com.red.core.util.JsonUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public class CacheInstance
{
	private String key;
	private byte[] value;
	private long ttl;

	public CacheInstance()
	{
	}

	public CacheInstance(String key)
	{
		this.key = key;
	}

	public CacheInstance(String key, Object value)
	{
		this.key = key;
		this.value = JsonUtil.toBytes(value);
	}

	static CacheInstance from(CacheMessage message)
	{
		CacheInstance instance = new CacheInstance(message.getKey());
		if (message.isCompress())
        {
            instance.value = CompressUtil.uncompress(message.getValue());
        }
		else
        {
            instance.value = message.getValue();
        }
		instance.ttl = message.getTtl();
		return instance;
	}

	CacheMessage build(byte state, CacheCommand command)
	{
		CacheMessage message = CacheMessage.create(state, command, key);
		if (value != null && value.length > Constant.COMPRESS_SIZE)
        {
            message.setValue(CompressUtil.compress(value));
            message.setCompress(true);
        }
		else
		{
            message.setValue(value);
        }
		message.setTtl(ttl);
		return message;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public byte[] getValue()
	{
		return value;
	}

	public <T> T getValueObject()
	{
		if (value == null || value.length == 0)
			return null;

		return JsonUtil.fromBytes(value);
	}

	public void setValueObject(Object object)
	{
		if (object == null)
			return;

		this.value = JsonUtil.toBytes(object);
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
