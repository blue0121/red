package com.red.server.queue;

import com.red.core.message.Message;
import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;

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
			queue.push(name, message);
		}

		Thread.sleep(1000);
		System.exit(0);
	}

}
