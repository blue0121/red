package com.red.client;

import com.red.client.net.NettyConnectionClient;
import com.red.client.registry.RegistryClientTest;
import com.red.client.registry.RegistryInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class ClientMain
{
	private static Logger logger = LoggerFactory.getLogger(ClientMain.class);

	public static final int TIMEOUT = 5000;
	public static final String TOKEN = "token";
	public static final String ADDRESS = "localhost:7903";

	public ClientMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		NettyConnectionClient client = new NettyConnectionClient(TIMEOUT, TOKEN, ADDRESS);
		try
		{
			client.start();
			registry(client);

			Thread.sleep(25_000);
		}
		finally
		{
			client.stop();
		}
	}

	private static void registry(NettyConnectionClient client) throws Exception
	{
		String name = "com.blue.Hello";
		String host = "localhost";
		int port = 8080;
		int port2 = 8082;

		RegistryClientTest registryClient = new RegistryClientTest(client);
		registryClient.save(name, host, port);
		registryClient.save(name, host, port2);
		RegistryInstance instance = registryClient.list(name);
		logger.info("host: {}", instance.getHostSet());
		registryClient.watch(name);
		registryClient.delete(name, host, port);
		registryClient.save(name, host, port);
		registryClient.delete(name, host, port);
		registryClient.delete(name, host, port2);
		registryClient.unwatch(name);
		instance = registryClient.list(name);
		logger.info("host: {}", instance.getHostSet());
	}

}
