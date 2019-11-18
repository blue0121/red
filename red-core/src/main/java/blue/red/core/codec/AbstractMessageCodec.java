package blue.red.core.codec;

import blue.red.core.message.Message;
import blue.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

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

	protected void writeString(String text, ByteBuf out)
	{
		if (text == null || text.isEmpty())
		{
			out.writeInt(0);
		}
		else
		{
			out.writeInt(text.length());
			out.writeCharSequence(text, StandardCharsets.UTF_8);
		}
	}

	protected String readString(ByteBuf in)
	{
		int len = in.readInt();
		if (len > 0)
		{
			CharSequence text = in.readCharSequence(len, StandardCharsets.UTF_8);
			return text.toString();
		}
		return null;
	}

	protected void writeBytes(byte[] bytes, ByteBuf out)
	{
		if (bytes == null || bytes.length == 0)
		{
			out.writeInt(0);
		}
		else
		{
			out.writeInt(bytes.length);
			out.writeBytes(bytes);
		}
	}

	protected byte[] readBytes(ByteBuf in)
	{
		int len = in.readInt();
		if (len > 0)
		{
			byte[] bytes = new byte[len];
			in.readBytes(bytes);
			return bytes;
		}
		return null;
	}

}
