package com.red.test.cucumber;

import com.red.client.config.RedConfig;
import com.red.client.net.NettyConnectionClient;
import com.red.client.registry.DefaultRegistryClient;
import com.red.client.registry.RegistryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-21
 */
public class RegistryClientFactory
{
	private static Logger logger = LoggerFactory.getLogger(RegistryClientFactory.class);

	private Map<String, NettyConnectionClient> clientMap = new HashMap<>();
	private Map<String, RegistryClient> registryClientMap = new HashMap<>();
	private RedConfig config = new RedConfig();
	private String address = "localhost:7903";

	private static RegistryClientFactory instance;

	private RegistryClientFactory()
	{
	}

	public static RegistryClientFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (RegistryClientFactory.class)
			{
				if (instance == null)
				{
					instance = new RegistryClientFactory();
				}
			}
		}
		return instance;
	}

	public void start(String name)
	{
		NettyConnectionClient client = new NettyConnectionClient(address, config);
		client.start();
		logger.info("start client {}", name);
		synchronized (this)
		{
			clientMap.put(name, client);
			registryClientMap.put(name, new DefaultRegistryClient(client));
		}
	}

	public synchronized void stop(String name)
	{
		NettyConnectionClient client = clientMap.get(name);
		if (client == null)
			return;

		client.stop();
		logger.info("stop client {}", name);
		clientMap.remove(name);
		registryClientMap.remove(name);
	}

	public synchronized void stopAll()
	{
		for (Map.Entry<String, NettyConnectionClient> entry : clientMap.entrySet())
		{
			entry.getValue().stop();
			logger.info("stop client {}", entry.getKey());
		}
		clientMap.clear();
		registryClientMap.clear();
	}

	public RegistryClient getRegistryClient(String name)
	{
		return registryClientMap.get(name);
	}

	public RegistryClient getRegistryClient()
	{
		if (registryClientMap.isEmpty())
			return null;

		Iterator<RegistryClient> iterator = registryClientMap.values().iterator();
		return iterator.next();
	}

}
