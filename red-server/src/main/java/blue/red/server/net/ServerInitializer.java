package blue.red.server.net;

import blue.red.core.codec.MessageCodeFactory;
import blue.red.core.codec.MessageCodec;
import blue.red.core.codec.MessageDecoder;
import blue.red.core.codec.MessageEncoder;
import blue.red.core.handler.HeartbeatHandler;
import blue.red.server.config.RedConfig;
import blue.red.server.config.RedConfigItem;
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
	private final RedConfig redConfig;
	private final int timeout;
	private final int heartbeat;
	private final String token;

	public ServerInitializer()
	{
		this.redConfig = RedConfig.getInstance();
		this.timeout = redConfig.getInt(RedConfigItem.SESSION_TIMEOUT, RedConfigItem.SESSION_TIMEOUT_VALUE);
		this.heartbeat = redConfig.getInt(RedConfigItem.SESSION_HEARTBEAT, RedConfigItem.SESSION_HEARTBEAT_VALUE);
		this.token = redConfig.getString(RedConfigItem.TOKEN, RedConfigItem.TOKEN_VALUE);
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline cp = ch.pipeline();
		MessageCodeFactory factory = MessageCodeFactory.getFactory();
		cp.addLast(new IdleStateHandler(timeout, heartbeat, heartbeat));
		cp.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, MessageCodec.ZERO_LENGTH,
				MessageCodec.LENGTH, MessageCodec.ZERO_LENGTH, MessageCodec.LENGTH));
		cp.addLast(new LengthFieldPrepender(MessageCodec.LENGTH));
		cp.addLast(new MessageEncoder(factory.getServerEncoderMap()));
		cp.addLast(new MessageDecoder(factory.getServerDecoderMap()));
		cp.addLast(new HeartbeatHandler());
		cp.addLast(new HandshakeHandler(token));
		cp.addLast(new RegistryHandler());
		cp.addLast(new CacheHandler());
	}

}
