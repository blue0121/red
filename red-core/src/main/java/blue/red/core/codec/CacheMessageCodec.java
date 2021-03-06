package blue.red.core.codec;

import blue.red.core.message.CacheCommand;
import blue.red.core.message.CacheMessage;
import blue.red.core.message.Message;
import io.netty.buffer.ByteBuf;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class CacheMessageCodec extends ResponseMessageCodec
{
	public CacheMessageCodec()
	{
	}

	@Override
	protected void encodeBody(Message message, ByteBuf out)
	{
		super.encodeBody(message, out);
		CacheMessage cache = (CacheMessage) message;
		out.writeByte(cache.getState());
		out.writeBoolean(cache.isCompress());
		out.writeShort(cache.getCommand().value());
		this.writeString(cache.getKey(), out);
		this.writeBytes(cache.getValue(), out);
		out.writeLong(cache.getTtl());
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		super.decodeBody(message, in);
		CacheMessage cache = (CacheMessage) message;
		cache.setState(in.readByte());
		cache.setCompress(in.readBoolean());
		cache.setCommand(CacheCommand.valueOf(in.readShort()));
		cache.setKey(this.readString(in));
		cache.setValue(this.readBytes(in));
		cache.setTtl(in.readLong());
	}

	@Override
	public Message getMessage()
	{
		return new CacheMessage();
	}
}
