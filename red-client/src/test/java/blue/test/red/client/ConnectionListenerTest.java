package blue.test.red.client;

import blue.red.client.ConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-05
 */
public class ConnectionListenerTest implements ConnectionListener
{
	private static Logger logger = LoggerFactory.getLogger(ConnectionListenerTest.class);

	public ConnectionListenerTest()
	{
	}

	@Override
	public void connected(InetSocketAddress address)
	{
		logger.info("Connected: {}", address);
	}

	@Override
	public void disconnected(InetSocketAddress address)
	{
		logger.info("Disconnected: {}", address);
	}
}
