package blue.test.red.client.registry;

import blue.red.client.ConnectionListener;
import blue.red.client.config.RedConfig;
import blue.red.client.net.NettyConnectionClient;
import blue.red.client.registry.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class RegistryClientTest
{
	private final RegistryCallback callback;
	private final RegistryClient registryClient;
	private final NettyConnectionClient client;

	public RegistryClientTest(String address, RedConfig config, ConnectionListener listener)
	{
		this(new NettyConnectionClient(address, config), listener);
	}

	public RegistryClientTest(NettyConnectionClient client, ConnectionListener listener)
	{
		this.callback = new RegistryListenerTest();
		this.client = client;
		this.client.addConnectionListener(listener);
		this.registryClient = new DefaultRegistryClient(client);
		this.client.start();
	}

	public void bind(String ip, int port)
	{
		RegistryInstance instance = new RegistryInstance();
		Host host = new Host(ip, port);
		instance.addHost(host);
		registryClient.bind(instance);
	}

	public void save(String...names)
	{
		RegistryInstance instance = new RegistryInstance(names);
		registryClient.saveSync(instance);
	}

	public void saveRate(long period, TimeUnit unit, String...names)
	{
		RegistryInstance instance = new RegistryInstance(names);
		registryClient.saveAtRate(instance, period, unit);
	}

	public void delete(String...names)
	{
		RegistryInstance instance = new RegistryInstance(names);
		registryClient.deleteSync(instance);
	}

	public RegistryInstance list(String name)
	{
		RegistryInstance instance = new RegistryInstance(name);
		return registryClient.listSync(instance);
	}

	public void watch(String...names)
	{
		RegistryInstance instance = new RegistryInstance(names);
		registryClient.watch(instance, callback);
	}

	public void unwatch(String...names)
	{
		RegistryInstance instance = new RegistryInstance(names);
		registryClient.unwatch(instance);
	}

	public void addConnectionListener(ConnectionListener listener)
	{
		this.client.addConnectionListener(listener);
	}

	public void close()
	{
		this.client.stop();
	}

}
