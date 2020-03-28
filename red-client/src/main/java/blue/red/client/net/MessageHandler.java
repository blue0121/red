package blue.red.client.net;

import blue.red.core.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message>
{
	private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private final NettyConnectionClient client;

	public MessageHandler(NettyConnectionClient client)
	{
		this.client = client;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Receive message: {}, 0x{}", message.getProtocol(), Long.toHexString(message.getMessageId()));
		}

		boolean returnMessage = client.getChannelClient().returnMessage(message);
		if (!returnMessage && client.getMessageListener() != null)
		{
			client.getMessageListener().complete(message);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		logger.warn("Error: ", cause);
	}
}
