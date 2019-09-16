package com.red.server.registry;

import com.red.core.message.RegistryItem;
import io.netty.channel.Channel;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public interface RegistryStorage
{

	void save(Set<String> nameSet, Channel channel);

	void delete(Set<String> nameSet, Channel channel);

	Set<RegistryItem> list(String name);

	void disconnect(RegistryItem item, Channel channel);

}
