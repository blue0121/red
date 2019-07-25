package com.red.server.queue;

import com.red.core.message.Message;
import com.red.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Jin Zheng
 * @since 2019-07-20
 */
public class ScheduledQueue<T extends Message> implements Queue<T>
{
	private static Logger logger = LoggerFactory.getLogger(ScheduledQueue.class);

	private final int partition;
	private final List<QueueHandler<T>> handlerList;
	private final List<BlockingQueue<MessageChannel<T>>> queueList;
	private final List<QueueThread<T>> threadList;

	private HashKey hashKey;

	public ScheduledQueue(QueueHandler<T> handler)
	{
		this(Collections.singletonList(handler));
	}

	public ScheduledQueue(List<QueueHandler<T>> handlerList)
	{
		AssertUtil.notEmpty(handlerList, "QueueHandler");
		this.partition = handlerList.size();
		this.handlerList = handlerList;
		this.hashKey = new DefaultHashKey();

		this.threadList = new ArrayList<>();
		this.queueList = new ArrayList<>();
		for (int i = 0; i < handlerList.size(); i++)
		{
			queueList.add(new LinkedBlockingQueue<>());
		}

		this.initThread();
		logger.info("initialize, partition: {}, handler: {}", partition, handlerList.size());
	}

	private void initThread()
	{
		for (int i = 0; i < handlerList.size(); i++)
		{
			QueueThread<T> thread = new QueueThread<>(queueList.get(i), handlerList.get(i));
			threadList.add(thread);
			thread.start();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			for (QueueThread<T> thread : threadList)
			{
				thread.interrupt();
			}
		}));
	}

	@Override
	public final void push(MessageChannel<T> data)
	{
		this.push(null, data);
	}

	@Override
	public final void push(String key, MessageChannel<T> data)
	{
		if (handlerList.size() > 1)
		{
			AssertUtil.notEmpty(key, "key");
		}
		AssertUtil.notNull(data, "message");
		int index = hashKey.calculate(key, partition);
		BlockingQueue<MessageChannel<T>> queue = queueList.get(index);
		queue.offer(data);
		logger.debug("push data to queue, index: {}, key: {}", index, key);
	}

	public void setHashKey(HashKey hashKey)
	{
		this.hashKey = hashKey;
	}

}
