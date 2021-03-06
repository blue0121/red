package blue.red.server.cache;

import blue.red.core.message.CacheMessage;
import blue.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-25
 */
public class SetCacheHandler implements CacheHandler
{
	private final CacheStorage storage;

	public SetCacheHandler(CacheStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(CacheMessage message, Channel channel)
	{
		CacheObject object = new CacheObject(message.getValue(), message.getTtl());
		storage.set(message.getKey(), object);
		CacheMessage response = message.toResponse(ResponseCode.SUCCESS, "Set successful");
		channel.writeAndFlush(response);
	}
}
