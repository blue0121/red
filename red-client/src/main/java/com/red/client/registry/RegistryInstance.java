package com.red.client.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class RegistryInstance
{
	public static final String SPLIT = ":";

	private String prefix;
	private String name;
	private List<Host> hostList = new ArrayList<>();

	public RegistryInstance()
	{
	}

	public RegistryInstance(String name)
	{
		this.name = name;
	}

	public RegistryInstance(String prefix, String name)
	{
		this.prefix = prefix;
		this.name = name;
	}

	public static RegistryInstance from(RegistryMessage message)
	{
		RegistryInstance instance = new RegistryInstance();
		int index = message.getName().indexOf(SPLIT);
		if (index != -1)
		{
			String prefix = message.getName().substring(0, index);
			String name = message.getName().substring(index + 1);
			instance.setPrefix(prefix);
			instance.setName(name);
		}
		else
		{
			instance.setName(message.getName());
		}
		for (String item : message.getItemList())
		{
			String[] items = item.split(SPLIT);
			instance.addHost(items[0], Integer.parseInt(items[1]));
		}
		return instance;
	}

	public RegistryMessage build(RegistryCommand command)
	{
		StringBuilder str = new StringBuilder();
		if (prefix != null && !prefix.isEmpty())
		{
			str.append(prefix).append(SPLIT);
		}
		str.append(name);
		RegistryMessage message = RegistryMessage.create(command, str.toString());
		for (Host host : hostList)
		{
			message.addItem(host.toString());
		}
		return message;
	}

	public void addHost(Host host)
	{
		if (!hostList.contains(host))
		{
			hostList.add(host);
		}
	}

	public void addHost(String ip, int port)
	{
		this.addHost(new Host(ip, port));
	}

	public Host getHost()
	{
		if (hostList.isEmpty())
			return null;

		return hostList.get(0);
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Host> getHostList()
	{
		return hostList;
	}
}
