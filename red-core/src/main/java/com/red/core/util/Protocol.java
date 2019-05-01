package com.red.core.util;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public enum Protocol
{
	HANDSHAKE(0x1),
	HEARTBEAT(0x2),
	CACHE(0x10);

	private int value;
	private int originalValue;

	Protocol(int value)
	{
		this.originalValue = value;
		this.value = Constant.PROTOCOL | (Constant.PROTOCOL_MASK & value);
	}

	public static Protocol valueOf(int value)
	{
		for (Protocol protocol : Protocol.values())
		{
			if (protocol.originalValue == value)
				return protocol;
		}
		return null;
	}

	public int value()
	{
		return this.value;
	}

	public int originalValue()
	{
		return this.originalValue;
	}

}
