package blue.red.server.cache;

import blue.red.core.message.CacheMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-25
 */
public class DeleteCacheHandler implements CacheHandler
{
	private final CacheStorage storage;

	public DeleteCacheHandler(CacheStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(CacheMessage message, Channel channel)
	{
		storage.delete(message.getKey());
		CacheMessage response = message.toResponse(ResponseCode.SUCCESS, "Delete successful");
		channel.writeAndFlush(response);
	}
}
