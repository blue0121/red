package com.red.server.registry;

import io.netty.channel.Channel;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public interface RegistryStorage
{

	void save(String name, Set<String> itemList);

	void delete(String name, String item);

	Set<String> list(String name);

	void watch(String name, Channel channel);

}
