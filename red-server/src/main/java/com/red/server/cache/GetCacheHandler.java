package com.red.server.cache;

import com.red.core.message.CacheMessage;
import com.red.core.message.ResponseCode;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-25
 */
public class GetCacheHandler implements CacheHandler
{
	private final CacheStorage storage;

	public GetCacheHandler(CacheStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void handle(CacheMessage message, Channel channel)
	{
		byte[] value = storage.get(message.getKey());
		CacheMessage response = message.toResponse(ResponseCode.SUCCESS, "Get successful");
		response.setValue(value);
		channel.writeAndFlush(response);
	}
}
