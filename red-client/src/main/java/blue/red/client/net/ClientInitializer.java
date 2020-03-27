package blue.red.client.net;

import blue.red.client.config.RedConfig;
import blue.red.core.codec.MessageCodeFactory;
import blue.red.core.codec.MessageCodec;
import blue.red.core.codec.MessageDecoder;
import blue.red.core.codec.MessageEncoder;
import blue.red.core.handler.HeartbeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel>
{
	private final NettyConnectionClient client;
	private final RedConfig redConfig;

	public ClientInitializer(NettyConnectionClient client)
	{
		this.client = client;
		this.redConfig = client.getRedConfig();
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline cp = ch.pipeline();
		MessageCodeFactory factory = MessageCodeFactory.getFactory();
		cp.addLast(new ReconnectHandler(client));
		cp.addLast(new IdleStateHandler(redConfig.getSessionTimeout(), redConfig.getSessionHeartbeat(), redConfig.getSessionHeartbeat()));
		cp.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, MessageCodec.ZERO_LENGTH,
				MessageCodec.LENGTH, MessageCodec.ZERO_LENGTH, MessageCodec.LENGTH));
		cp.addLast(new LengthFieldPrepender(MessageCodec.LENGTH));
		cp.addLast(new MessageEncoder(factory.getClientEncoderMap()));
		cp.addLast(new MessageDecoder(factory.getClientDecoderMap()));
		cp.addLast(new HeartbeatHandler());
		cp.addLast(new HandshakeHandler(client));
		cp.addLast(new MessageHandler(client));
	}

}
