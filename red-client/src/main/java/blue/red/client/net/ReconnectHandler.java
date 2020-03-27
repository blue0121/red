package blue.red.client.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2020-03-27
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter
{
	private static Logger logger = LoggerFactory.getLogger(ReconnectHandler.class);

	private final NettyConnectionClient client;
	private final RetryPolicy retryPolicy;

	private int retries = 0;

	public ReconnectHandler(NettyConnectionClient client)
	{
		this.client = client;
		this.retryPolicy = client.getRetryPolicy();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		this.retries = 0;
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		this.retries++;
		Channel channel = ctx.channel();
		client.getChannelClient().handlerDisconnected(channel);
		boolean allow = retryPolicy.allowRetry(retries);
		if (!client.isStop() && allow)
		{
			int sleep = retryPolicy.getSleepTimeMs(retries);
			logger.warn("Disconnected, reconnect after {} ms: {}", sleep, client.getRemoteAddress());
			channel.eventLoop().schedule(() -> client.connect(), sleep, TimeUnit.MILLISECONDS);
		}
		super.channelInactive(ctx);
	}
}
