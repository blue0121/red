package com.red.server.queue;

import com.red.core.message.Message;

/**
 * @author Jin Zheng
 * @since 2019-07-20
 */
public interface Queue<T extends Message>
{

	void push(MessageChannel<T> data);

	void push(String key, MessageChannel<T> data);

}
