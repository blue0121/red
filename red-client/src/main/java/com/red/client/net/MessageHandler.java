package com.red.client.net;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.red.client.CallbackClient;
import com.red.client.HandlerClient;
import com.red.core.message.Message;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message> implements HandlerClient
{
	private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private final NettyConnectionClient client;
	private final Cache<Long, FutureClient> requestCache;
	private Channel channel;
	private CallbackClient callback;

	public MessageHandler(NettyConnectionClient client)
	{
		this.client = client;
		requestCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Receive message: {}, 0x{}", message.getProtocol(), Long.toHexString(message.getMessageId()));
		}

		FutureClient future = requestCache.getIfPresent(message.getMessageId());
		if (callback != null)
		{
			callback.complete(message);
		}
		if (future != null)
		{
			requestCache.invalidate(message.getMessageId());
			future.done(message);
		}
	}

	@Override
	public Future<Message> invoke(Message request)
	{
		return this.invoke(request, null);
	}

	@Override
	public Future<Message> invoke(Message request, CallbackClient callback)
	{
		ChannelFuture cf = channel.writeAndFlush(request);
		if (logger.isDebugEnabled())
		{
			cf.addListener(new ChannelFutureListener()
			{
				@Override
				public void operationComplete(ChannelFuture future) throws Exception
				{
					if (future.isSuccess())
					{
						logger.debug("Send message successful: {}, 0x{}", request.getProtocol(), Long.toHexString(request.getMessageId()));
					}
					else
					{
						logger.warn("Send message failure: {}, 0x{}", request.getProtocol(), Long.toHexString(request.getMessageId()));
					}
				}
			});
		}
		FutureClient future = new FutureClient(request, callback);
		requestCache.put(request.getMessageId(), future);
		return future;
	}

	@Override
	public void onReceive(CallbackClient callback)
	{
		this.callback = callback;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		this.channel = ctx.channel();
		this.client.addHandlerClient(channel.id().asLongText(), this);
		if (client.getCallback() != null)
		{
			this.callback = client.getCallback();
		}

		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		this.client.removeHandlerClient(channel.id().asLongText());
		this.channel = null;
		this.callback = null;

		super.channelInactive(ctx);
	}
}
