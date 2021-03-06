package blue.test.red.test.cucumber;

import blue.red.client.registry.Host;
import blue.red.client.registry.RegistryCallback;
import blue.red.client.registry.RegistryClient;
import blue.red.client.registry.RegistryInstance;
import blue.red.test.cucumber.ClientFactory;
import blue.red.test.cucumber.RegistryInstanceMap;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-21
 */
public class RegistrySteps
{
	private static Logger logger = LoggerFactory.getLogger(RegistrySteps.class);

	public static final String NAME = "name";
	public static final String HOST = "host";
	public static final String HOSTS_SPLIT = ",";
	public static final String HOST_SPLIT = ":";

	private ClientFactory clientFactory = ClientFactory.getInstance();
	private RegistryInstanceMap instanceMap = RegistryInstanceMap.getInstance();

	public RegistrySteps()
	{
	}

	@After
	public void stopAll()
	{
		clientFactory.stopAll();
		instanceMap.clear();
	}

	@Given("start registry client {string}")
	public void start(String name)
	{
		clientFactory.start(name);
	}

	@Given("stop registry client {string}")
	public void stop(String name)
	{
		clientFactory.stop(name);
	}

	@Given("registry client {string} {string} {string}")
	public void bind(String name, String opt, String host)
	{
		RegistryClient client = clientFactory.getRegistryClient(name);
		Assertions.assertNotNull(client);
		RegistryInstance instance = new RegistryInstance();
		instance.addHost(Host.parse(host));
		if ("bind".equals(opt))
		{
			client.bind(instance);
		}
		else if ("unbind".equals(opt))
		{
			client.unbind(instance);
		}
		else
		{
			Assertions.fail("error opt: " + opt);
		}
	}

	@Given("registry client {string} watch:")
	public void watch(String name, List<Map<String, String>> dataTable)
	{
		Assertions.assertTrue(dataTable != null && !dataTable.isEmpty());
		RegistryClient client = clientFactory.getRegistryClient(name);
		Assertions.assertNotNull(client);

		RegistryInstance instance = new RegistryInstance();
		for (Map<String, String> data : dataTable)
		{
			instance.addName(data.get(NAME));
		}
		client.watch(instance, new RegistryCallback() {
			@Override
			public void onSuccess(RegistryInstance data)
			{
				instanceMap.add(name, data);
			}

			@Override
			public void onFailure(Exception e)
			{
				logger.error("error, ", e);
			}
		});
		logger.info("watch, {} - {}", name, instance.getNameSet());
	}

	@When("registry client {string} {string}:")
	public void handle(String name, String opt, List<Map<String, String>> dataTable)
	{
		Assertions.assertTrue(dataTable != null && !dataTable.isEmpty());
		RegistryClient client = clientFactory.getRegistryClient(name);
		Assertions.assertNotNull(client);

		RegistryInstance nameInstance = new RegistryInstance();
		for (Map<String, String> data : dataTable)
		{
			nameInstance.addName(data.get(NAME));
		}
		try
		{
			if ("save".equals(opt))
			{
				client.saveSync(nameInstance);
				logger.info("Save, name: {}", nameInstance.getNameSet());
			}
			else if ("delete".equals(opt))
			{
				client.deleteSync(nameInstance);
				logger.info("Delete, name: {}", nameInstance.getNameSet());
			}
			else if ("get".equals(opt))
			{
				Map<String, RegistryInstance> map = this.getRegistryInstance(dataTable);
				for (Map.Entry<String, RegistryInstance> entry : map.entrySet())
				{
					RegistryInstance dest = client.listSync(entry.getValue());
					Assertions.assertEquals(entry.getValue().getHostSet(), dest.getHostSet());
				}
			}
		}
		catch (Exception e)
		{
			instanceMap.add(name, e);
			logger.error("Error, ", e);
		}
	}

	@When("sleep {int} seconds")
	public void sleep(int seconds)
	{
		try
		{
			Thread.sleep(seconds * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Then("verify registry client {string} can receive:")
	public void verify(String client, List<Map<String, String>> dataTable)
	{
		Map<String, Set<Host>> map = instanceMap.get(client);
		Map<String, RegistryInstance> instance = this.getRegistryInstance(dataTable);
		//System.out.println(map);
		//System.out.println(instance);
		if ((instance == null || instance.isEmpty()) && (map == null || map.isEmpty()))
			return;

		for (Map.Entry<String, RegistryInstance> entry : instance.entrySet())
		{
			if (map == null && (entry.getValue().getHostSet() == null || entry.getValue().getHostSet().isEmpty()))
				continue;

			Set<Host> hostSet = map.get(entry.getKey());
			if ((entry.getValue().getHostSet() == null || entry.getValue().getHostSet().isEmpty())
				&& (hostSet == null && hostSet.isEmpty()))
				continue;

			Assertions.assertEquals(entry.getValue().getHostSet(), hostSet);
		}
	}

	@Then("verify registry client {string} throw exception {string}")
	public void verifyException(String client, String exception)
	{
		Set<String> set = instanceMap.getException(client);
		Assertions.assertTrue(set != null && set.contains(exception));
	}

	@Then("verify registry client {string} can not receive:")
	public void verifyNot(String client, List<Map<String, String>> dataTable)
	{
		Map<String, Set<Host>> map = instanceMap.get(client);
		Map<String, RegistryInstance> instance = this.getRegistryInstance(dataTable);
		if (map == null || map.isEmpty())
			return;

		for (Map.Entry<String, RegistryInstance> entry : instance.entrySet())
		{
			Set<Host> hostSet = map.get(entry.getKey());
			if (hostSet == null || hostSet.isEmpty())
				continue;

			for (Host host : entry.getValue().getHostSet())
			{
				Assertions.assertFalse(hostSet.contains(host));
			}
		}
	}

	@Then("clear registry client {string}")
	public void clear(String client)
	{
		instanceMap.clear(client);
	}

	private Map<String, RegistryInstance> getRegistryInstance(List<Map<String, String>> dataTable)
	{
		Map<String, RegistryInstance> map = new HashMap<>();
		if (dataTable == null || dataTable.isEmpty())
			return map;

		for (Map<String, String> data : dataTable)
		{
			String name = data.get(NAME);
			if (name == null || name.isEmpty())
				continue;

			String host = data.get(HOST);
			RegistryInstance instance = map.computeIfAbsent(name, n -> new RegistryInstance(n));
			if (host == null || host.isEmpty())
				continue;

			for (String h : host.split(HOSTS_SPLIT))
			{
				String[] hh = h.trim().split(HOST_SPLIT);
				Host host2 = new Host(hh[0].trim(), Integer.parseInt(hh[1].trim()));
				instance.addHost(host2);
			}
		}
		return map;
	}

}
