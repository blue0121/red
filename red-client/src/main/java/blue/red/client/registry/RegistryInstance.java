package blue.red.client.registry;

import blue.red.core.message.RegistryCommand;
import blue.red.core.message.RegistryItem;
import blue.red.core.message.RegistryMessage;

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

	public static RegistryInstance from(RegistryMessage message)
	{
		RegistryInstance instance = new RegistryInstance();
		instance.addNameList(message.getNameSet());
		for (RegistryItem item : message.getItemSet())
		{
			Host host = Host.parse(item.getItem());
			host.setToken(item.getToken());
			instance.addHost(host);
		}
		return instance;
	}

	public RegistryMessage build(RegistryCommand command)
	{
		RegistryMessage message = RegistryMessage.create(command, nameSet.toArray(new String[0]));
		for (Host host : hostSet)
		{
			RegistryItem item = new RegistryItem(host.toAddr(), host.getToken());
			message.addItem(item);
		}
		return message;
	}

	public void addHost(Host host)
	{
		hostSet.add(host);
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

	@Override
	public String toString()
	{
		return String.format("RegistryInstance[name=%s, item=%s]", nameSet, hostSet);
	}
}
