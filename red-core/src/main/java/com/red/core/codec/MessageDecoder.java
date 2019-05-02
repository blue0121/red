package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class MessageDecoder extends ByteToMessageDecoder
{
	private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
	private final Map<Protocol, MessageCodec> codecMap;

	public MessageDecoder(Map<Protocol, MessageCodec> codecMap)
	{
		this.codecMap = codecMap;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception
	{
		Protocol protocol = Protocol.valueOf(in.readInt());
		if (protocol == null)
		{
			logger.warn("Protocol mismatch");
			return;
		}
		MessageCodec codec = codecMap.get(protocol);
		if (codec == null)
		{
			logger.error("No found MessageCodec from: {}", protocol);
			return;
		}
		Message message = codec.decode(protocol, in);
		list.add(message);
	}

}
