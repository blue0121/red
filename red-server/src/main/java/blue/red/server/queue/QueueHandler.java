package blue.red.server.queue;

import blue.red.core.message.Message;

/**
 * @author Jin Zheng
 * @since 2019-07-20
 */
public interface QueueHandler<T extends Message>
{

	void handle(MessageChannel<T> data);

}
