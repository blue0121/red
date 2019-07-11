package com.red.server.registry;

import io.netty.channel.Channel;

import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public interface RegistryStorage
{

	void save(Set<String> nameSet, Channel channel);

	void delete(Set<String> nameSet, Channel channel);

	Set<String> list(String name);

	void disconnect(Channel channel);

	ExecutorService getExecutorService();

}
