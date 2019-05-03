package com.red.client.net;

import com.red.client.ConnectionClient;
import com.red.client.RedClientException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class NettyConnectionClient implements ConnectionClient
{
	private static Logger logger = LoggerFactory.getLogger(NettyConnectionClient.class);

	private final int timeout;
	private final String token;
	private final InetSocketAddress remoteAddress;
	private ClientInitializer initializer;

	private boolean handshake = false;
	private boolean stop = false;
	private Bootstrap bootstrap;
	private EventLoopGroup workerGroup;
	private ChannelFuture channelFuture;

	public NettyConnectionClient(int timeout, String token, String address)
	{
		this.timeout = timeout;
		this.token = token;

		String[] addrs = address.split(":");
		if (addrs.length != 2)
			throw new RedClientException("Invalid address: " + address);

		this.remoteAddress = new InetSocketAddress(addrs[0], Integer.parseInt(addrs[1]));
		this.initializer = new ClientInitializer(this);
	}

	@Override
	public void start()
	{
		workerGroup = new NioEventLoopGroup(4);
		bootstrap = new Bootstrap();
		bootstrap.group(workerGroup)
				.channel(NioSocketChannel.class).
				option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
				.handler(initializer);

		this.connect();
	}

	public void connect()
	{
		bootstrap.connect(remoteAddress).addListener(new ChannelFutureListener()
		{
			@Override
			public void operationComplete(ChannelFuture future) throws Exception
			{
				if (!future.isSuccess())
				{
					logger.warn("Disconnected, reconnect after {} ms: {}", timeout, remoteAddress);
					future.channel().eventLoop().schedule(() -> connect(), timeout, TimeUnit.MILLISECONDS);
					return;
				}
				channelFuture = future;
				logger.info("Connected successful: {}", remoteAddress);
			}
		});
	}

	@Override
	public void stop()
	{
		stop = true;
		if (channelFuture != null)
		{
			channelFuture.channel().flush().close().syncUninterruptibly();
			channelFuture = null;
			logger.debug("Channel closed.");
		}
		if (workerGroup != null)
		{
			workerGroup.shutdownGracefully();
			workerGroup = null;
			logger.debug("EventLoopGroup closed.");
		}
		logger.info("Disconnected successful: {}", remoteAddress);
	}

	public boolean isStop()
	{
		return stop;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public String getToken()
	{
		return token;
	}

	public InetSocketAddress getRemoteAddress()
	{
		return remoteAddress;
	}

	public boolean isHandshake()
	{
		return handshake;
	}

	public void setHandshake(boolean handshake)
	{
		this.handshake = handshake;
	}
}