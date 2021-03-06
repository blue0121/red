package blue.red.server.net;

import blue.red.core.message.CacheMessage;
import blue.red.server.cache.CacheHandlerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class CacheHandler extends SimpleChannelInboundHandler<CacheMessage>
{
	private static Logger logger = LoggerFactory.getLogger(CacheHandler.class);

	public CacheHandler()
	{
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CacheMessage message) throws Exception
	{
		CacheHandlerFactory.getFactory().handle(message, ctx.channel());
	}

}
