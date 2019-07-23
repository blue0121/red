package com.red.server.queue;

import com.red.core.message.Message;

import java.nio.channels.Channel;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-22
 */
public class MessageChannel<T extends Message>
{
	private T message;
	private Channel channel;

	public MessageChannel(T message, Channel channel)
	{
		this.message = message;
		this.channel = channel;
	}

	public T getMessage()
	{
		return message;
	}

	public Channel getChannel()
	{
		return channel;
	}

}
