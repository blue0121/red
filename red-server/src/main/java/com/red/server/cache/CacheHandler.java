package com.red.server.cache;

import com.red.core.message.CacheMessage;
import io.netty.channel.Channel;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public interface CacheHandler
{

	void handle(CacheMessage message, Channel channel);

}
