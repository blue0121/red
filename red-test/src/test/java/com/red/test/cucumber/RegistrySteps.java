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

	@Given("registry client {string} watch:")
	public void watch(String name, List<Map<String, String>> dataTable)
	{
		if (dataTable == null || dataTable.isEmpty())
		{
			logger.warn("No watch");
			return;
		}
		RegistryClient client = clientFactory.getRegistryClient(name);
		if (client == null)
		{
			logger.error("No registry client");
			return;
		}

		RegistryInstance instance = new RegistryInstance();
		for (Map<String, String> data : dataTable)
		{
			instance.addName(data.get("name"));
		}
		client.watch(instance, ri -> instanceMap.add(name, ri) );
		logger.info("watch, {} - {}", name, instance.getNameSet());
	}

	@When("registry client {string} {string}:")
	public void handle(String name, String opt, List<Map<String, String>> dataTable)
	{
		Map<String, RegistryInstance> map = this.getRegistryInstance(dataTable);
		if (map == null || map.isEmpty())
		{
			logger.warn("No registry instance");
			return;
		}
		RegistryClient client = clientFactory.getRegistryClient(name);
		if (client == null)
		{
			logger.error("No registry client");
			return;
		}

		for (Map.Entry<String, RegistryInstance> entry : map.entrySet())
		{
			if ("save".equals(opt))
			{
				client.saveSync(entry.getValue());
				logger.info("save, {} - {}", entry.getKey(), entry.getValue().getHostSet());
			}
			else if ("delete".equals(opt))
			{
				client.deleteSync(entry.getValue());
				logger.info("delete, {} - {}", entry.getKey(), entry.getValue().getHostSet());
			}
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

	@Then("registry client {string} get:")
	public void get(String name, List<Map<String, String>> dataTable)
	{
		RegistryClient client = clientFactory.getRegistryClient(name);
		if (client == null)
		{
			logger.error("No registry client");
			return;
		}
		Map<String, RegistryInstance> map = this.getRegistryInstance(dataTable);
		if (map == null || map.isEmpty())
			return;

		for (Map.Entry<String, RegistryInstance> entry : map.entrySet())
		{
			RegistryInstance dest = client.listSync(entry.getValue());
			Assert.assertEquals(entry.getValue().getHostSet(), dest.getHostSet());
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
			String name = data.get("name");
			if (name == null || name.isEmpty())
				continue;

			String host = data.get("host");
			RegistryInstance instance = map.computeIfAbsent(name, n -> new RegistryInstance(n));
			if (host == null || host.isEmpty())
				continue;

			for (String h : host.split(","))
			{
				String[] hh = h.split(":");
				instance.addHost(hh[0], Integer.parseInt(hh[1]));
			}
		}
		return map;
	}

}
