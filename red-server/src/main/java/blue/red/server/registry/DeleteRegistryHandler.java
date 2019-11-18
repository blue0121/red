package blue.red.server.registry;

import blue.red.core.message.RegistryMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class DeleteRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public DeleteRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getNameSet().isEmpty())
			throw new RegistryStorageException("name is empty");

		storage.delete(message.getNameSet(), channel);
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "Delete successful");
		channel.writeAndFlush(response);
	}
}
