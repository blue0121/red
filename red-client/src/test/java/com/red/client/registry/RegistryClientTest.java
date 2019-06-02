package com.red.client.registry;

import com.red.client.net.NettyConnectionClient;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryClientTest
{
	private final RegistryListener callback;
	private final RegistryClient client;

	public RegistryClientTest(NettyConnectionClient client)
	{
		this.callback = new RegistryListenerTest();
		this.client = new DefaultRegistryClient(client);
	}

	public void save(String name, String ip, int port) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(name);
		instance.addHost(ip, port);
		client.saveSync(instance);
	}

	public void delete(String name, String ip, int port) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(name);
		instance.addHost(ip, port);
		client.deleteSync(instance);
	}

	public RegistryInstance list(String name) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(name);
		return client.listSync(instance);
	}

	public void watch(String name)
	{
		RegistryInstance instance = new RegistryInstance(name);
		client.watch(instance, callback);
	}

	public void unwatch(String name)
	{
		RegistryInstance instance = new RegistryInstance(name);
		client.unwatch(instance);
	}

}
