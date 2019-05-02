package com.red.core.codec;

import com.red.core.message.HandshakeMessage;
import com.red.core.message.Message;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

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
		if (handshake.getToken() == null || handshake.getToken().isEmpty())
		{
			out.writeInt(0);
		}
		else
		{
			out.writeInt(handshake.getToken().length());
			out.writeCharSequence(handshake.getToken(), StandardCharsets.UTF_8);
		}
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		HandshakeMessage handshake = (HandshakeMessage) message;
		int len = in.readInt();
		if (len > 0)
		{
			CharSequence token = in.readCharSequence(len, StandardCharsets.UTF_8);
			handshake.setToken(token.toString());
		}
	}

	@Override
	public Message getMessage()
	{
		return new HandshakeMessage();
	}
}
