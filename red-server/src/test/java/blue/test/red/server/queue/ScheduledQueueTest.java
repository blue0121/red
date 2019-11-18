package blue.test.red.server.queue;

import blue.red.core.message.Message;
import blue.red.core.message.RegistryCommand;
import blue.red.core.message.RegistryMessage;
import blue.red.server.queue.MessageChannel;
import blue.red.server.queue.QueueHandler;
import blue.red.server.queue.ScheduledQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-07-21
 */
public class ScheduledQueueTest
{
	public static final int SIZE = 10;
	public static final int QUEUE = 2;

	public ScheduledQueueTest()
	{
	}

	public static void main(String[] args) throws Exception
	{
		List<QueueHandler<Message>> handlerList = new ArrayList<>();
		for (int i = 0; i < QUEUE; i++)
		{
			handlerList.add(new QueueHandlerTest());
		}


		ScheduledQueue<Message> queue = new ScheduledQueue<>(handlerList);
		for (int i = 0; i < SIZE; i++)
		{
			String name = "blue" + i;
			Message message = RegistryMessage.create(RegistryCommand.SAVE, name);

			queue.push(name, new MessageChannel<>(message, null));
		}

		Thread.sleep(1000);
		System.exit(0);
	}

}
