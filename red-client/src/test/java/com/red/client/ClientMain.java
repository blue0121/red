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
		String prefix = "blue";
		String name = "com.blue.Hello";
		String host = "localhost";
		int port = 8080;
		int port2 = 8082;

		RegistryClientTest registryClient = new RegistryClientTest(client);
		registryClient.save(prefix, name, host, port);
		registryClient.save(prefix, name, host, port2);
		RegistryInstance instance = registryClient.list(prefix, name);
		logger.info("host: {}", instance.getHostList());
		registryClient.watch(prefix, name);
		registryClient.delete(prefix, name, host, port);
		registryClient.save(prefix, name, host, port);
		//registryClient.unwatch(prefix, name);
		registryClient.delete(prefix, name, host, port);
		registryClient.delete(prefix, name, host, port2);
		instance = registryClient.list(prefix, name);
		logger.info("host: {}", instance.getHostList());
	}

}
