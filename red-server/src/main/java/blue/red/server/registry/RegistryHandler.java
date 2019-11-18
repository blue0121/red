package blue.red.server.registry;

import blue.red.core.message.RegistryMessage;
import io.netty.channel.Channel;


/**
 * @author Jin Zheng
 * @since 1.0 2019-05-10
 */
public interface RegistryHandler
{

	void handle(RegistryMessage message, Channel channel);

}
