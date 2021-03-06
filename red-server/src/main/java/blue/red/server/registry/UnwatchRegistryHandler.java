package blue.red.server.registry;

import blue.red.core.message.RegistryMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class UnwatchRegistryHandler implements RegistryHandler
{
	private final RegistryChannelGroup channelGroup;

	public UnwatchRegistryHandler(RegistryChannelGroup channelGroup)
	{
		this.channelGroup = channelGroup;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getNameSet().isEmpty())
			throw new RegistryStorageException("name is empty");

		for (String name : message.getNameSet())
		{
			channelGroup.unwatchChannel(name, channel);
		}
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Unwatch successful");
		channel.writeAndFlush(response);
	}
}
