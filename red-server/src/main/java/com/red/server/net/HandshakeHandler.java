package com.red.server.net;

import com.red.core.message.HandshakeMessage;
import com.red.core.message.Response;
import com.red.core.message.ResponseCode;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HandshakeHandler extends SimpleChannelInboundHandler<HandshakeMessage>
{
	private static Logger logger = LoggerFactory.getLogger(HandshakeHandler.class);

	private final String token;

	public HandshakeHandler(String token)
	{
		this.token = token;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HandshakeMessage request) throws Exception
	{
		if (token == null || token.isEmpty())
			return;

		ResponseCode code = ResponseCode.SUCCESS;
		String message = "Handshake successful";
		if (token.equals(request.getToken()))
		{
			logger.info(message);
			Response response = Response.from(request, code, message);
			ctx.writeAndFlush(response);
		}
		else
		{
			code = ResponseCode.HANDSHAKE;
			message = "Handshake failure";
			logger.warn("Handshake failure, expect: {}, but actual: {}", token, request.getToken());
			Response response = Response.from(request, code, message);
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
