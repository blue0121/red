package com.red.client;

import com.red.core.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public interface HandlerClient
{

	Future sendMessage(Message request);

	Future sendMessage(Message request, MessageListener listener);

	void sendMessageAtFixRate(Message message, long period, TimeUnit unit);

	MessageListener getMessageListener();

	void addConnectionListener(ConnectionListener listener);

	void setRegistryListener(MessageListener registryListener);

}
