package com.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class Message
{
	protected Protocol protocol;
	protected Version version;
	protected long messageId;

	public Message()
	{
	}

	public void setProtocol(int protocol)
	{
		this.protocol = Protocol.valueOf(protocol);
	}

	public void setVersion(int version)
	{
		this.version = Version.valueOf(version);
	}

	public Protocol getProtocol()
	{
		return protocol;
	}

	public void setProtocol(Protocol protocol)
	{
		this.protocol = protocol;
	}

	public Version getVersion()
	{
		return version;
	}

	public void setVersion(Version version)
	{
		this.version = version;
	}

	public long getMessageId()
	{
		return messageId;
	}

	public void setMessageId(long messageId)
	{
		this.messageId = messageId;
	}
}
