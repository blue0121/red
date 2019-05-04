package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.Response;
import io.netty.buffer.ByteBuf;

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
		this.writeString(response.getMessage(), out);
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		Response response = (Response) message;
		response.setCode(in.readInt());
		response.setMessage(this.readString(in));
	}

	@Override
	public Message getMessage()
	{
		return new Response();
	}
}
