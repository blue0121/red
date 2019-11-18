package blue.red.server.cache;

import blue.red.core.message.CacheMessage;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-08
 */
public class CacheObject
{
	private byte[] value;
	private long ttl;
	private long timestamp;

	public CacheObject()
	{
		this.timestamp = System.currentTimeMillis();
	}

	public CacheObject(byte[] value, long ttl)
	{
		this.value = value;
		this.ttl = ttl;
		this.timestamp = System.currentTimeMillis();
	}

	public static CacheObject from(CacheMessage message)
	{
		CacheObject object = new CacheObject();
		object.setValue(message.getValue());
		object.setTtl(message.getTtl());
		return object;
	}

	public long expire()
	{
		if (ttl <= 0)
			return ttl;

		return ttl + timestamp - System.currentTimeMillis();
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

	public long getTimestamp()
	{
		return timestamp;
	}
}
