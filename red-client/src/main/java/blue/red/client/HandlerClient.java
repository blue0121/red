package blue.red.client;

import blue.red.core.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public interface HandlerClient
{

	RedFuture<Message> sendMessage(Message request);

	RedFuture<Message> sendMessage(Message request, MessageListener listener);

	void sendMessageAtFixRate(Message message, long period, TimeUnit unit);

	MessageListener getMessageListener();

	void addConnectionListener(ConnectionListener listener);

	void setRegistryListener(MessageListener registryListener);

	void setCacheListener(MessageListener cacheListener);

}
