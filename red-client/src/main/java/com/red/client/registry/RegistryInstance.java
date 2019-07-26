package com.red.client.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class RegistryInstance
{
	public static final String SPLIT = ":";

	private Set<String> nameSet = new HashSet<>();
	private Set<Host> hostSet = new HashSet<>();

	public RegistryInstance()
	{
	}

	public RegistryInstance(String...names)
	{
		for (String name : names)
		{
			nameSet.add(name);
		}
	}

	static RegistryInstance from(RegistryMessage message)
	{
		RegistryInstance instance = new RegistryInstance();
		instance.addNameList(message.getNameSet());
		for (String item : message.getItemSet())
		{
			String[] items = item.split(SPLIT);
			instance.addHost(items[0], Integer.parseInt(items[1]));
		}
		return instance;
	}

	RegistryMessage build(RegistryCommand command)
	{
		RegistryMessage message = RegistryMessage.create(command, nameSet.toArray(new String[0]));
		for (Host host : hostSet)
		{
			message.addItem(host.toString());
		}
		return message;
	}

	public void addHost(Host host)
	{
		hostSet.add(host);
	}

	public void addHost(String ip, int port)
	{
		hostSet.add(new Host(ip, port));
	}

	public void addHostList(Collection<Host> hostList)
	{
		if (hostList == null || hostList.isEmpty())
			return;

		hostSet.addAll(hostList);
	}

	public void addName(String name)
	{
		nameSet.add(name);
	}

	public void addNameList(Collection<String> nameList)
	{
		if (nameList == null || nameList.isEmpty())
			return;

		nameSet.addAll(nameList);
	}

	public Host getHost()
	{
		if (hostSet.isEmpty())
			return null;

		return hostSet.iterator().next();
	}

	public String getName()
	{
		if (nameSet.isEmpty())
			return null;

		return nameSet.iterator().next();
	}

	public Set<Host> getHostSet()
	{
		return hostSet;
	}

	public Set<String> getNameSet()
	{
		return nameSet;
	}
}
