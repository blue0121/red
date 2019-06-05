package com.red.client;

import com.red.client.net.NettyConnectionClient;
import com.red.client.registry.RegistryClientTest;
import com.red.client.registry.RegistryInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

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
		ConnectionListener connectionListener = new ConnectionListenerTest();
		NettyConnectionClient client1 = new NettyConnectionClient(TIMEOUT, TOKEN, ADDRESS);
		client1.addConnectionListener(connectionListener);
		NettyConnectionClient client2 = new NettyConnectionClient(TIMEOUT, TOKEN, ADDRESS);
		client2.addConnectionListener(connectionListener);
		client1.start();
		client2.start();

		rate(client1, client2);
		//reg(client1, client2);
	}

	public static void rate(NettyConnectionClient client1, NettyConnectionClient client2) throws Exception
	{
		String name = "com.blue.Hello";
		String host = "localhost";
		int port1 = 8000;

		RegistryClientTest registry1 = new RegistryClientTest(client1);
		RegistryClientTest registry2 = new RegistryClientTest(client2);

		registry2.watch(name);
		registry1.saveRate(name, host, port1, 2, TimeUnit.SECONDS);


		Thread.sleep(21_000);
		client1.stop();
		client2.stop();
	}

	private static void reg(NettyConnectionClient client1, NettyConnectionClient client2) throws Exception
	{
		String name = "com.blue.Hello";
		String host = "localhost";
		int port1 = 8000;
		int port2 = 9000;

		RegistryClientTest registry1 = new RegistryClientTest(client1);
		RegistryClientTest registry2 = new RegistryClientTest(client2);

		registry1.watch(name);
		registry2.watch(name);

		registry1.save(name, host, port1);
		registry2.save(name, host, port2);

		RegistryInstance instance = registry1.list(name);
		logger.info("host: {}", instance.getHostSet());

		Thread.sleep(10_000);
		client1.stop();

		Thread.sleep(10_000);
		client2.stop();

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
