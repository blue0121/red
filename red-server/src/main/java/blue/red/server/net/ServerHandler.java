package blue.red.server.net;

import blue.red.core.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message>
{
	public ServerHandler()
	{
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
	{

	}

}
