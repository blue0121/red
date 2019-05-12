package com.red.server.registry;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public interface RegistryStorage
{

	void save(String name, Collection<String> itemList);

	void delete(String name, String item);

	Set<String> list(String name);

	void watch(String name, Channel channel);

	void unwatch(String name, Channel channel);

	ExecutorService getExecutorService();

}
