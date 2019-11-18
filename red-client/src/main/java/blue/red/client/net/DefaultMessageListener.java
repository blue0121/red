package blue.red.client.net;

import blue.red.client.MessageListener;
import blue.red.core.message.Message;
import blue.red.core.message.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class DefaultMessageListener implements MessageListener
{
	private static Logger logger = LoggerFactory.getLogger(DefaultMessageListener.class);

	private MessageListener registryListener;
	private MessageListener cacheListener;
	private final ExecutorService executorService;

	public DefaultMessageListener(ExecutorService executorService)
	{
		this.executorService = executorService;
	}

	@Override
	public void complete(Message message)
	{
		if (message.getProtocol() == Protocol.REGISTRY && registryListener != null)
		{
			executorService.submit(() ->registryListener.complete(message));
		}
		else if (message.getProtocol() == Protocol.CACHE && cacheListener != null)
		{
			executorService.submit(() -> cacheListener.complete(message));
		}
	}

	public void setRegistryListener(MessageListener registryListener)
	{
		this.registryListener = registryListener;
	}

	public void setCacheListener(MessageListener cacheListener)
	{
		this.cacheListener = cacheListener;
	}

}
