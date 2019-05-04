package com.red.core.codec;

import com.red.core.message.HandshakeMessage;
import com.red.core.message.Message;
import io.netty.buffer.ByteBuf;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HandshakeMessageCodec extends AbstractMessageCodec
{
	public HandshakeMessageCodec()
	{
	}

	@Override
	protected void encodeBody(Message message, ByteBuf out)
	{
		HandshakeMessage handshake = (HandshakeMessage) message;
		this.writeString(handshake.getToken(), out);
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		HandshakeMessage handshake = (HandshakeMessage) message;
		handshake.setToken(this.readString(in));
	}

	@Override
	public Message getMessage()
	{
		return new HandshakeMessage();
	}
}
