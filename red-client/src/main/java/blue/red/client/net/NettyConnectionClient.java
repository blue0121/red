package blue.red.client.net;

import blue.red.client.ConnectionClient;
import blue.red.client.ConnectionListener;
import blue.red.client.HandlerClient;
import blue.red.client.MessageListener;
import blue.red.client.RedClientException;
import blue.red.client.RedFuture;
import blue.red.client.config.RedConfig;
import blue.red.core.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class NettyConnectionClient implements ConnectionClient, HandlerClient
{
	private static Logger logger = LoggerFactory.getLogger(NettyConnectionClient.class);

	private final RedConfig redConfig;
	private final InetSocketAddress remoteAddress;
	private ClientInitializer initializer;

	private boolean stop = false;
	private Bootstrap bootstrap;
	private EventLoopGroup workerGroup;
	private Channel channel;
	private final ChannelClient channelClient;
	private final DefaultMessageListener messageListener;
	private final ExecutorService executorService;
	private final RetryPolicy retryPolicy;

	public NettyConnectionClient(String address, RedConfig redConfig)
	{
		String[] addrs = address.split(":");
		if (addrs.length != 2)
			throw new RedClientException("Invalid address: " + address);

		this.redConfig = redConfig;
		this.executorService = Executors.newFixedThreadPool(10);
		this.channelClient = new ChannelClient(executorService);
		this.remoteAddress = new InetSocketAddress(addrs[0], Integer.parseInt(addrs[1]));
		this.initializer = new ClientInitializer(this);
		this.messageListener = new DefaultMessageListener(executorService);
		this.retryPolicy = new DefaultRetryPolicy(redConfig.getConnectTimeout() * RedConfig.MILLS);

		this.init();
	}

	private void init()
	{
		workerGroup = new NioEventLoopGroup(redConfig.getNettyThread());
		bootstrap = new Bootstrap();
		bootstrap.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, redConfig.getConnectTimeout() * RedConfig.MILLS)
				.handler(initializer);
	}

	@Override
	public void start()
	{
		this.connect();
		channelClient.waitHandshake();
	}

	public void connect()
	{
		synchronized (bootstrap)
		{
			ChannelFuture future = bootstrap.connect(remoteAddress);
			future.addListener(new ChannelFutureListener()
			{
				@Override
				public void operationComplete(ChannelFuture future) throws Exception
				{
					if (!future.isSuccess())
					{
						future.channel().pipeline().fireChannelInactive();
						return;
					}
					channel = future.channel();
					logger.info("Connected successful: {}", remoteAddress);
				}
			});
		}
	}

	@Override
	public void stop()
	{
		stop = true;
		if (channel != null)
		{
			channel.flush().close().syncUninterruptibly();
			channel = null;
			logger.debug("Channel closed.");
		}
		if (workerGroup != null)
		{
			workerGroup.shutdownGracefully();
			workerGroup = null;
			logger.debug("EventLoopGroup closed.");
		}
		executorService.shutdown();
		logger.info("Disconnected successful: {}", remoteAddress);
	}

	@Override
	public RedFuture<Message> sendMessage(Message message)
	{
		return channelClient.sendMessage(message, null);
	}

	@Override
	public RedFuture<Message> sendMessage(Message message, MessageListener listener)
	{
		RedFuture<Message> future = channelClient.sendMessage(message, listener);
		Message response = channelClient.getMessage(message.getMessageId());
		if (response != null)
		{
			((FutureClient)future).done(response);
		}
		return future;
	}

	@Override
	public void sendMessageAtFixRate(Message message, long period, TimeUnit unit)
	{
		channelClient.sendMessageAtFixRate(message, period, unit);
	}

	@Override
	public void addConnectionListener(ConnectionListener listener)
	{
		channelClient.addConnectionLister(listener);
	}

	@Override
	public void setRegistryListener(MessageListener registryListener)
	{
		this.messageListener.setRegistryListener(registryListener);
	}

	@Override
	public void setCacheListener(MessageListener cacheListener)
	{
		this.messageListener.setCacheListener(cacheListener);
	}

	@Override
	public MessageListener getMessageListener()
	{
		return this.messageListener;
	}

	public boolean isStop()
	{
		return stop;
	}

	public RedConfig getRedConfig()
	{
		return redConfig;
	}

	public InetSocketAddress getRemoteAddress()
	{
		return remoteAddress;
	}

	public ChannelClient getChannelClient()
	{
		return channelClient;
	}

	public RetryPolicy getRetryPolicy()
	{
		return retryPolicy;
	}
}
