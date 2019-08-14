package com.red.core.message;

import com.red.core.util.Constant;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class CacheMessage extends Response
{
	public static final byte TRANSIENT = 0x1;
	public static final byte PERSISTENCE = 0x2;

	private byte state;
	private boolean compress;
	private CacheCommand command;
	private String key;
	private byte[] value;
	private long ttl;

	public CacheMessage()
	{
	}

	public static CacheMessage createPersistence(CacheCommand command, String key)
	{
		return create(PERSISTENCE, command, key);
	}

	public static CacheMessage createTransient(CacheCommand command, String key)
	{
		return create(TRANSIENT, command, key);
	}

	public static CacheMessage create(byte state, CacheCommand command, String key)
	{
		CacheMessage message = new CacheMessage();
		message.setProtocol(Protocol.CACHE);
		message.setVersion(Constant.DEFAULT_VERSION);
		message.setMessageId(SingleSnowflakeId.getInstance().nextId());
		message.setCode(ResponseCode.SUCCESS);
		message.setState(state);
		message.setCommand(command);
		message.setKey(key);
		return message;
	}

	public CacheMessage toResponse(ResponseCode code, String message)
	{
		CacheMessage response = new CacheMessage();
		response.setProtocol(protocol);
		response.setVersion(version);
		response.setMessageId(messageId);
		response.setCode(code);
		response.setMessage(message);
		response.setState(state);
		response.setCompress(compress);
		response.setCommand(command);
		response.setKey(key);
		return response;
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

	public boolean isCompress()
	{
		return compress;
	}

	public void setCompress(boolean compress)
	{
		this.compress = compress;
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

	public long getTtl()
	{
		return ttl;
	}

	public void setTtl(long ttl)
	{
		this.ttl = ttl;
	}
}
