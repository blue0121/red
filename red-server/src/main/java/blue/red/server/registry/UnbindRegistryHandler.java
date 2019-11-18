package blue.red.server.registry;

import blue.red.core.message.RegistryMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-11
 */
public class UnbindRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public UnbindRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		storage.disconnect(message.getItem(), channel);
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Unbind successful");
		channel.writeAndFlush(response);
	}
}
