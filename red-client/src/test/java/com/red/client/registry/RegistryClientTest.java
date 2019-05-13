package com.red.client.registry;

import com.red.client.net.NettyConnectionClient;

import java.util.concurrent.Future;

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
		Future<RegistryInstance> future = client.saveAsync(instance, callback);
		future.get();
	}

	public void delete(String prefix, String name, String ip, int port) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		instance.addHost(ip, port);
		Future<RegistryInstance> future = client.deleteAsync(instance, callback);
		future.get();
	}

	public RegistryInstance list(String prefix, String name) throws Exception
	{
		RegistryInstance instance = new RegistryInstance(prefix, name);
		Future<RegistryInstance> future = client.listAsync(instance, callback);
		return future.get();
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
