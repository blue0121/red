package com.red.client;

import com.red.core.message.Message;

import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public interface HandlerClient
{

	Future<Message> sendMessage(Message request);

	Future<Message> sendMessage(Message request, MessageListener listener);

	MessageListener getCallback();

	void addConnectionListener(ConnectionListener listener);

	void setRegistryCallback(MessageListener registryCallback);

}
