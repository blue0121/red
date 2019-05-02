package com.red.server.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class Server
{
	private static Logger logger = LoggerFactory.getLogger(Server.class);

	private final int port;
	private final ServerInitializer initializer;

	public Server()
	{
		this(7903);
	}

	public Server(int port)
	{
		this.port = port;
		this.initializer = new ServerInitializer();
	}

	public void start() throws Exception
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childHandler(initializer);

			ChannelFuture cf = bootstrap.bind(port).sync();
			logger.info("Server started, bind port: {}", port);

			cf.channel().closeFuture().sync();
			logger.info("Server closed.");
		}
		finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}

}
