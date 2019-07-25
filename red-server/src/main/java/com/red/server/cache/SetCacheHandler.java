package com.red.server.cache;

import com.red.core.message.CacheMessage;
import com.red.core.message.ResponseCode;
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
		storage.save(message.getKey(), message.getValue(), message.getTtl());
		CacheMessage response = message.toResponse(ResponseCode.SUCCESS, "Set successful");
		channel.writeAndFlush(response);
	}
}
