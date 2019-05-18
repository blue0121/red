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

	Future<Message> sendMessage(Message request, CallbackClient callback);

	CallbackClient getCallback();

	void setRegistryCallback(CallbackClient registryCallback);

}
