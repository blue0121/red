package com.red.core.message;

import com.red.core.util.Constant;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class HeartbeatMessage extends Message
{
	public static final byte PING = 0x1;
	public static final byte PONG = 0x2;

	protected byte type;

	public HeartbeatMessage()
	{
	}

	public static HeartbeatMessage createPing()
	{
		HeartbeatMessage ping = new HeartbeatMessage();
		ping.setProtocol(Protocol.HEARTBEAT);
		ping.setVersion(Constant.DEFAULT_VERSION);
		ping.setMessageId(SingleSnowflakeId.getInstance().nextId());
		ping.setType(PING);
		return ping;
	}

	public HeartbeatMessage toPong()
	{
		HeartbeatMessage pong = new HeartbeatMessage();
		pong.setProtocol(protocol);
		pong.setVersion(version);
		pong.setMessageId(messageId);
		pong.setType(PONG);
		return pong;
	}

	public boolean isPing()
	{
		return type == PING;
	}

	public boolean isPong()
	{
		return type == PONG;
	}

	public byte getType()
	{
		return type;
	}

	public void setType(byte type)
	{
		this.type = type;
	}
}
