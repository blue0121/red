package blue.red.server.queue;

import blue.red.core.message.Message;
import io.netty.channel.Channel;


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
