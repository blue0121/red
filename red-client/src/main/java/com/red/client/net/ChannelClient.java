package com.red.client.net;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.red.client.CallbackClient;
import com.red.client.RedClientException;
import com.red.core.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class ChannelClient
{
	private static Logger logger = LoggerFactory.getLogger(ChannelClient.class);

	private final ChannelGroup channelGroup;
	private final Random random;
	private final Cache<Long, FutureClient> requestCache;
	private final CountDownLatch latch;

	public ChannelClient()
	{
		this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		this.random = new Random();
		this.latch = new CountDownLatch(1);
		requestCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
	}

	public void waitHandshake()
	{
		try
		{
			this.latch.await();
		}
		catch (InterruptedException e)
		{
			logger.warn("Interrupted", e);
		}
	}

	public void addChannel(Channel channel)
	{
		channelGroup.add(channel);
		this.latch.countDown();
		logger.info("Add channel: {}", channel.id());
	}

	public boolean returnMessage(Message message)
	{
		FutureClient future = requestCache.getIfPresent(message.getMessageId());
		if (future == null)
			return false;

		requestCache.invalidate(message.getMessageId());
		future.done(message);
		return true;
	}

	public Future<Message> sendMessage(Message message, CallbackClient callback)
	{
		if (message == null)
			throw new NullPointerException("Message must be not null");

		int size = channelGroup.size();
		if (size == 0)
			throw new RedClientException("No handler client!");

		Channel[] channels = channelGroup.toArray(new Channel[0]);
		if (channels.length == 1)
		{
			channels[0].writeAndFlush(message).addListener(new SenderListener(message));
		}
		else
		{
			int index = random.nextInt(channels.length);
			channels[index].writeAndFlush(message).addListener(new SenderListener(message));
		}
		FutureClient future = new FutureClient(message, callback);
		requestCache.put(message.getMessageId(), future);
		return future;
	}

	class SenderListener implements ChannelFutureListener
	{
		private final Message message;

		public SenderListener(Message message)
		{
			this.message = message;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception
		{
			if (future.isSuccess())
			{
				logger.debug("Send message successful: {}, 0x{}", message.getProtocol(), Long.toHexString(message.getMessageId()));
			}
			else
			{
				logger.warn("Send message failure: {}, 0x{}", message.getProtocol(), Long.toHexString(message.getMessageId()));
			}
			Throwable throwable = future.cause();
			if (throwable != null)
			{
				logger.error("Occur exception: ", throwable);
			}
		}
	}

}
