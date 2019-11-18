package blue.red.client;

import blue.red.core.message.Message;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public interface MessageListener
{

	void complete(Message message);

}
