package com.red.server.queue;

import com.red.core.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * @author Jin Zheng
 * @since 2019-07-21
 */
public class QueueThread<T extends Message> extends Thread
{
	private static Logger logger = LoggerFactory.getLogger(QueueThread.class);

	private final BlockingQueue<T> queue;
	private final QueueHandler<T> handler;

	public QueueThread(BlockingQueue<T> queue, QueueHandler<T> handler)
	{
		this.queue = queue;
		this.handler = handler;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				T data = queue.take();
				if (logger.isDebugEnabled())
				{
					logger.debug("take data from queue, id: 0x{}", Long.toHexString(data.getMessageId()));
				}
				try
				{
					handler.handle(data);
				}
				catch (Exception e)
				{
					logger.error("Error, ", e);
				}
				catch (Error e)
				{
					logger.error("Error, ", e);
				}
			}
		}
		catch (InterruptedException e)
		{
			logger.warn("interrupted, exit loop");
		}
	}

}
