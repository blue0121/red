package com.red.server.net;

import com.red.core.codec.MessageCodeFactory;
import com.red.core.codec.MessageDecoder;
import com.red.core.codec.MessageEncoder;
import com.red.core.handler.HeartbeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel>
{
	public ServerInitializer()
	{
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline cp = ch.pipeline();
		MessageCodeFactory factory = MessageCodeFactory.getFactory();
		cp.addLast(new IdleStateHandler(30, 10, 10));
		cp.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		cp.addLast(new LengthFieldPrepender(4));
		cp.addLast(new MessageEncoder(factory.getServerEncoderMap()));
		cp.addLast(new MessageDecoder(factory.getServerDecoderMap()));
		cp.addLast(new HeartbeatHandler());
		cp.addLast(new HandshakeHandler("token"));
		cp.addLast(new RegistryHandler());
	}

}
