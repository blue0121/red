package com.red.server.registry;

import com.red.core.message.RegistryMessage;

import java.nio.channels.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public interface RegistryHandler
{

	void handle(RegistryMessage message, Channel channel);

}
