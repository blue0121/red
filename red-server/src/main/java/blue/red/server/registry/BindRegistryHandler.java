package blue.red.server.registry;

import blue.red.core.message.RegistryMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-11
 */
public class BindRegistryHandler implements RegistryHandler
{
	private final RegistryChannelGroup channelGroup;

	public BindRegistryHandler(RegistryChannelGroup channelGroup)
	{
		this.channelGroup = channelGroup;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getItemSet().size() != 1)
			throw new RegistryChannelException("item size must be 1");

		channelGroup.bindChannel(message.getItem(), channel);
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Bind successful");
		channel.writeAndFlush(response);
	}
}
