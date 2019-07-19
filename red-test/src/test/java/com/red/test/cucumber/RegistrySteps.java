package com.red.test.cucumber;

import com.red.client.registry.Host;
import com.red.client.registry.RegistryClient;
import com.red.client.registry.RegistryInstance;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
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

	private RegistryClientFactory clientFactory = RegistryClientFactory.getInstance();
	private RegistryInstanceMap instanceMap = RegistryInstanceMap.getInstance();

	public RegistrySteps()
	{
	}

	@After
	public void stop()
	{
		clientFactory.stopAll();
		instanceMap.clear();
	}

	@Given("start registry client {string}")
	public void start(String name)
	{
		clientFactory.start(name);
	}

	@Given("registry client {string} {string} {string}")
	public void bind(String name, String opt, String host)
	{
		RegistryClient client = clientFactory.getRegistryClient(name);
		Assert.assertNotNull("No Registry Client", client);
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
			Assert.fail("error opt: " + opt);
		}
	}

	@Given("registry client {string} watch:")
	public void watch(String name, List<Map<String, String>> dataTable)
	{
		Assert.assertTrue("No Data", dataTable != null && !dataTable.isEmpty());
		RegistryClient client = clientFactory.getRegistryClient(name);
		Assert.assertNotNull("No Registry Client", client);

		RegistryInstance instance = new RegistryInstance();
		for (Map<String, String> data : dataTable)
		{
			instance.addName(data.get(NAME));
		}
		client.watch(instance, ri -> instanceMap.add(name, ri) );
		logger.info("watch, {} - {}", name, instance.getNameSet());
	}

	@When("registry client {string} {string}:")
	public void handle(String name, String opt, List<Map<String, String>> dataTable)
	{
		Assert.assertTrue("No Data", dataTable != null && !dataTable.isEmpty());
		RegistryClient client = clientFactory.getRegistryClient(name);
		Assert.assertNotNull("No Registry Client", client);

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
					Assert.assertEquals(entry.getValue().getHostSet(), dest.getHostSet());
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
		if (map == null || map.isEmpty())
		{
			if (instance != null && !instance.isEmpty())
			{
				Assert.fail("can not receive");
				return;
			}
		}

		for (Map.Entry<String, RegistryInstance> entry : instance.entrySet())
		{
			Assert.assertEquals(entry.getValue().getHostSet(), map.get(entry.getKey()));
		}
	}

	@Then("verify registry client {string} throw exception {string}")
	public void verifyException(String client, String exception)
	{
		Set<String> set = instanceMap.getException(client);
		Assert.assertTrue("Verify exception", set != null && set.contains(exception));
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
				Assert.assertFalse(hostSet.contains(host));
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
				instance.addHost(hh[0].trim(), Integer.parseInt(hh[1].trim()));
			}
		}
		return map;
	}

}
