package blue.red.server.queue;

import blue.red.core.message.Message;
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

	private final BlockingQueue<MessageChannel<T>> queue;
	private final QueueHandler<T> handler;

	public QueueThread(BlockingQueue<MessageChannel<T>> queue, QueueHandler<T> handler)
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
				MessageChannel<T> data = queue.take();
				if (logger.isDebugEnabled() && data.getMessage() != null)
				{
					logger.debug("take data from queue, id: 0x{}", Long.toHexString(data.getMessage().getMessageId()));
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
