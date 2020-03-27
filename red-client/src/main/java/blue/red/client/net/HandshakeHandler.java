package blue.red.client.net;

import blue.red.client.config.RedConfig;
import blue.red.core.message.HandshakeMessage;
import blue.red.core.message.Protocol;
import blue.red.core.message.Response;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class HandshakeHandler extends SimpleChannelInboundHandler<Response>
{
	private static Logger logger = LoggerFactory.getLogger(HandshakeHandler.class);

	private final NettyConnectionClient client;
	private final RedConfig redConfig;
	private Channel channel;

	public HandshakeHandler(NettyConnectionClient client)
	{
		this.client = client;
		this.redConfig = client.getRedConfig();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception
	{
		if (response.getProtocol() == Protocol.HANDSHAKE)
		{
			if (response.getCode() == ResponseCode.SUCCESS)
			{
				logger.info("Handshake successful");
				client.getChannelClient().addChannel(channel);
			}
			else
			{
				logger.warn("0x{}: {}", Integer.toHexString(response.getCode().code()), response.getMessage());
				client.stop();
			}
		}
		else
		{
			ctx.fireChannelRead(response);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		channel = ctx.channel();
		HandshakeMessage request = HandshakeMessage.create(redConfig.getToken());
		ctx.writeAndFlush(request);
		logger.info("Handshake to {}: {}", client.getRemoteAddress(), redConfig.getToken());

		super.channelActive(ctx);
	}

}
