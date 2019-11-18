package blue.red.core.codec;

import blue.red.core.message.Message;
import blue.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class MessageEncoder extends MessageToByteEncoder<Message>
{
	private static Logger logger = LoggerFactory.getLogger(MessageEncoder.class);
	private final Map<Protocol, MessageCodec> codecMap;

	public MessageEncoder(Map<Protocol, MessageCodec> codecMap)
	{
		this.codecMap = codecMap;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception
	{
		if (message == null)
		{
			logger.warn("Message is null");
			return;
		}
		MessageCodec codec = codecMap.get(message.getProtocol());
		if (codec == null)
		{
			logger.error("No found MessageCodec from: {}", message.getProtocol());
			return;
		}
		codec.encode(message, out);
	}

}
