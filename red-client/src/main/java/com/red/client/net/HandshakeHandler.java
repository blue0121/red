package com.red.client.net;

import com.red.core.message.HandshakeMessage;
import com.red.core.message.Protocol;
import com.red.core.message.Response;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class HandshakeHandler extends SimpleChannelInboundHandler<Response>
{
	private static Logger logger = LoggerFactory.getLogger(HandshakeHandler.class);

	private final NettyConnectionClient client;
	private Channel channel;

	public HandshakeHandler(NettyConnectionClient client)
	{
		this.client = client;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception
	{
		if (response.getProtocol() == Protocol.HANDSHAKE)
		{
			if (response.getCode() == ResponseCode.SUCCESS)
			{
				logger.info("Handshake successful");
				client.setHandshake(true);
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
		HandshakeMessage request = HandshakeMessage.create(client.getToken());
		ctx.writeAndFlush(request);
		logger.info("Handshake to {}: {}", client.getRemoteAddress(), client.getToken());

		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		if (!client.isStop())
		{
			logger.warn("Disconnected, reconnect after {} ms: {}", client.getTimeout(), client.getRemoteAddress());
			channel.eventLoop().schedule(() -> client.connect(), client.getTimeout(), TimeUnit.MILLISECONDS);
		}

		super.channelInactive(ctx);
	}
}
