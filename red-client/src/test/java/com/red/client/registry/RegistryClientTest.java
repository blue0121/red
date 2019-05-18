package com.red.client.registry;

import com.red.client.net.NettyConnectionClient;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryClientTest
{
	private final RegistryCallback callback;
	private final RegistryClient client;

	public RegistryClientTest(NettyConnectionClient client)
	{
		this.callback = new RegistryCallbackTest();
		this.client = new DefaultRegistryClient(client);
	}

	public void save(String prefix, String name, String ip, int port) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		instance.addHost(ip, port);
		client.saveSync(instance);
	}

	public void delete(String prefix, String name, String ip, int port) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		instance.addHost(ip, port);
		client.deleteSync(instance);
	}

	public RegistryInstance list(String prefix, String name) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		return client.listSync(instance);
	}

	public void watch(String prefix, String name)
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		client.watch(instance, callback);
	}

	public void unwatch(String prefix, String name)
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		client.unwatch(instance);
	}

}
