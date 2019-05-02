package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.Response;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class ResponseMessageCodec extends AbstractMessageCodec
{
	public ResponseMessageCodec()
	{
	}

	@Override
	protected void encodeBody(Message message, ByteBuf out)
	{
		Response response = (Response) message;
		out.writeInt(response.getCode().code());
		if (response.getMessage() == null || response.getMessage().isEmpty())
		{
			out.writeInt(0);
		}
		else
		{
			out.writeInt(response.getMessage().length());
			out.writeCharSequence(response.getMessage(), StandardCharsets.UTF_8);
		}
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		Response response = (Response) message;
		response.setCode(in.readInt());
		int len = in.readInt();
		if (len > 0)
		{
			CharSequence msg = in.readCharSequence(len, StandardCharsets.UTF_8);
			response.setMessage(msg.toString());
		}
	}

	@Override
	public Message getMessage()
	{
		return new Response();
	}
}
