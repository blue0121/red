package com.red.test.cucumber;

import com.red.client.registry.Host;
import com.red.client.registry.RegistryInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-02
 */
public class RegistryInstanceMap
{
	private static Logger logger = LoggerFactory.getLogger(RegistryInstanceMap.class);

	private Map<String, Map<String, Set<Host>>> instanceMap = new HashMap<>();
	private Map<String, Set<String>> exceptionMap = new HashMap<>();

	private static RegistryInstanceMap instance;

	private RegistryInstanceMap()
	{
	}

	public static RegistryInstanceMap getInstance()
	{
		if (instance != null)
			return instance;

		synchronized (RegistryInstanceMap.class)
		{
			if (instance != null)
				return instance;

			instance = new RegistryInstanceMap();
			return instance;
		}
	}

	public synchronized void add(String client, RegistryInstance instance)
	{
		if (client == null || client.isEmpty()
			|| instance == null || instance.getNameSet().isEmpty() || instance.getHostSet().isEmpty())
			return;

		Map<String, Set<Host>> hostMap = instanceMap.computeIfAbsent(client, k -> new HashMap<>());
		for (String name : instance.getNameSet())
		{
			Set<Host> hostList = hostMap.computeIfAbsent(name, k -> new HashSet<>());
			hostList.addAll(instance.getHostSet());
			logger.info("add registry instance, vin: {}, name: {}, host: {}", client, name, instance.getHostSet());
		}
	}

	public Map<String, Set<Host>> get(String client)
	{
		if (client == null || client.isEmpty())
			return null;

		return instanceMap.get(client);
	}

	public synchronized void clear(String client)
	{
		if (client == null || client.isEmpty())
			return;

		instanceMap.remove(client);
		logger.info("clear client {}", client);
	}

	public synchronized void clear(String client, RegistryInstance instance)
	{
		if (client == null || client.isEmpty() || instance == null || instance.getNameSet().isEmpty())
			return;

		exceptionMap.remove(client);
		Map<String, Set<Host>> hostMap = instanceMap.get(client);
		for (String name : instance.getNameSet())
		{
			hostMap.remove(name);
			logger.info("clear, client: {}, name: {}", client, name);
		}
	}

	public synchronized void add(String client, Exception e)
	{
		Set<String> set = exceptionMap.computeIfAbsent(client, k -> new HashSet<>());
		set.add(e.getClass().getName());
	}

	public synchronized Set<String> getException(String client)
	{
		return exceptionMap.get(client);
	}

	public synchronized void clear()
	{
		instanceMap.clear();
		exceptionMap.clear();
		logger.info("clear all");
	}

}
