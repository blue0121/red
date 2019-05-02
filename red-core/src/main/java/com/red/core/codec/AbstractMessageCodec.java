package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public abstract class AbstractMessageCodec implements MessageCodec
{
	public AbstractMessageCodec()
	{
	}

	@Override
	public final void encode(Message message, ByteBuf out)
	{
		out.writeInt(message.getProtocol().value());
		out.writeInt(message.getVersion().value());
		out.writeLong(message.getMessageId());
		this.encodeBody(message, out);
	}

	@Override
	public final Message decode(Protocol protocol, ByteBuf in)
	{
		Message message = this.getMessage();
		message.setProtocol(protocol);
		message.setVersion(in.readInt());
		message.setMessageId(in.readLong());
		this.decodeBody(message, in);
		return message;
	}

	protected abstract void encodeBody(Message message, ByteBuf out);

	protected abstract void decodeBody(Message message, ByteBuf in);

}
