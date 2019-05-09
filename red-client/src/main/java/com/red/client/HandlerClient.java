package com.red.client;

import com.red.core.message.Message;

import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public interface HandlerClient
{

	Future<Message> invoke(Message request);

	Future<Message> invoke(Message request, CallbackClient callback);

	void onReceive(CallbackClient callback);

}
