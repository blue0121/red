package com.red.server.queue;

import com.red.core.message.Message;

/**
 * @author Jin Zheng
 * @since 2019-07-20
 */
public interface QueueHandler<T extends Message>
{

	void handle(T data);

}
