package blue.red.core.codec;

import blue.red.core.message.HeartbeatMessage;
import blue.red.core.message.Message;
import io.netty.buffer.ByteBuf;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HeartbeatMessageCodec extends AbstractMessageCodec
{
	public HeartbeatMessageCodec()
	{
	}

	@Override
	protected void encodeBody(Message message, ByteBuf out)
	{
		HeartbeatMessage heartbeat = (HeartbeatMessage) message;
		out.writeByte(heartbeat.getType());
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		HeartbeatMessage heartbeat = (HeartbeatMessage) message;
		heartbeat.setType(in.readByte());
	}

	@Override
	public Message getMessage()
	{
		return new HeartbeatMessage();
	}
}
