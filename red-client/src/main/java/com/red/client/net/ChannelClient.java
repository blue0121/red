package com.red.client.net;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.red.client.ConnectionListener;
import com.red.client.MessageListener;
import com.red.client.RedClientException;
import com.red.core.message.Message;
import com.red.core.util.AssertUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public class ChannelClient
{
	private static Logger logger = LoggerFactory.getLogger(ChannelClient.class);

	private final ChannelGroup channelGroup;
	private final Random random;
	private final Cache<Long, FutureClient> futureCache;
	private final Cache<Long, Message> messageCache;
	private final Set<ConnectionListener> connectionListenerSet;
	private final CountDownLatch latch;

	public ChannelClient()
	{
		this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		this.random = new Random();
		this.latch = new CountDownLatch(1);
		this.futureCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
		this.messageCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
		this.connectionListenerSet = new CopyOnWriteArraySet<>();
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
		this.handlerConnected(channel);
		this.latch.countDown();
		logger.info("Add channel: {}", channel.id());
	}

	public boolean returnMessage(Message message)
	{
		FutureClient future = futureCache.getIfPresent(message.getMessageId());
		if (future == null)
		{
			messageCache.put(message.getMessageId(), message);
			if (logger.isDebugEnabled())
			{
				logger.debug("No found future from: {}, 0x{}", message.getProtocol(), Long.toHexString(message.getMessageId()));
			}
			return false;
		}

		futureCache.invalidate(message.getMessageId());
		future.done(message);
		return true;
	}

	public void sendMessageAtFixRate(Message message, long period, TimeUnit unit)
	{
		AssertUtil.notNull(message, "Message");
		AssertUtil.positive(period, "Period");
		AssertUtil.notNull(unit, "TimeUnit");

		Channel channel = this.getChannel();
		channel.eventLoop().scheduleAtFixedRate(() -> channel.writeAndFlush(message), 0, period, unit);
	}

	public Future<Message> sendMessage(Message message, MessageListener listener)
	{
		AssertUtil.notNull(message, "Message");

		Channel channel = this.getChannel();
		channel.writeAndFlush(message).addListener(new SenderListener(message));
		FutureClient future = new FutureClient(message, listener);
		futureCache.put(message.getMessageId(), future);
		return future;
	}

	public void addConnectionLister(ConnectionListener listener)
	{
		AssertUtil.notNull(listener, "ConnectionLister");
		connectionListenerSet.add(listener);
	}

	private Channel getChannel()
	{
		int size = channelGroup.size();
		if (size == 0)
			throw new RedClientException("No handler client!");

		Channel[] channels = channelGroup.toArray(new Channel[0]);
		if (channels.length == 1)
		{
			return channels[0];
		}
		else
		{
			int index = random.nextInt(channels.length);
			return channels[index];
		}
	}

	private void handlerConnected(Channel channel)
	{
		if (connectionListenerSet.isEmpty())
			return;

		InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
		for (ConnectionListener listener : connectionListenerSet)
		{
			listener.connected(address);
		}
	}

	public void handlerDisconnected(Channel channel)
	{
		if (connectionListenerSet.isEmpty())
			return;

		InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
		for (ConnectionListener listener : connectionListenerSet)
		{
			listener.disconnected(address);
		}
	}

	public Message getMessage(long messageId)
	{
		return messageCache.getIfPresent(messageId);
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
