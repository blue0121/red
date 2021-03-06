package blue.red.server.registry;

import blue.red.core.message.RegistryItem;
import blue.red.core.message.RegistryMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-05-11
 */
public class ListRegistryHandler implements RegistryHandler
{
	private final RegistryStorage storage;

	public ListRegistryHandler(RegistryStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(RegistryMessage message, Channel channel)
	{
		if (message.getNameSet().size() != 1)
			throw new RegistryStorageException("name size must be 1");

		Set<RegistryItem> itemSet = storage.list(message.getName());
		RegistryMessage response = message.toResponse(ResponseCode.SUCCESS, "List successful");
		response.addItemList(itemSet);
		channel.writeAndFlush(response);
	}
}
