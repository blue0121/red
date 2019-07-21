package com.red.server.queue;

import com.red.core.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-07-21
 */
public class QueueHandlerTest implements QueueHandler<Message>
{
	private static Logger logger = LoggerFactory.getLogger(QueueHandlerTest.class);

	public QueueHandlerTest()
	{
	}

	@Override
	public void handle(Message data)
	{
		logger.info("receive data: 0x{}, {}", Long.toHexString(data.getMessageId()), data.getProtocol());
	}
}
