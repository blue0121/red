package com.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class CacheRequest extends Message
{
	public static final byte TRANSIENT = 0x1;
	public static final byte PERSISTENCE = 0x2;

	private byte state;
	private CacheCommand command;
	private String key;
	private byte[] value;
	private int ttl;

	public CacheRequest()
	{
	}

	public boolean isTransient()
	{
		return state == TRANSIENT;
	}

	public boolean isPersistence()
	{
		return state == PERSISTENCE;
	}

	public byte getState()
	{
		return state;
	}

	public void setState(byte state)
	{
		this.state = state;
	}

	public CacheCommand getCommand()
	{
		return command;
	}

	public void setCommand(CacheCommand command)
	{
		this.command = command;
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

	public void setValue(byte[] value)
	{
		this.value = value;
	}

	public int getTtl()
	{
		return ttl;
	}

	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}
}
