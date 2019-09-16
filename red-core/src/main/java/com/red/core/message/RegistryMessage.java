package com.red.core.message;

import com.red.core.util.Constant;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-05-04
 */
public class RegistryMessage extends Response
{
	private RegistryCommand command;
	private Set<String> nameSet = new HashSet<>();
	private Set<RegistryItem> itemSet = new HashSet<>();

	public RegistryMessage()
	{
	}

	public static RegistryMessage create(RegistryCommand command, String...names)
	{
		RegistryMessage message = new RegistryMessage();
		message.setProtocol(Protocol.REGISTRY);
		message.setVersion(Constant.DEFAULT_VERSION);
		message.setMessageId(SingleSnowflakeId.getInstance().nextId());
		message.setCode(ResponseCode.SUCCESS);
		message.setCommand(command);
		for (String name : names)
		{
			message.addName(name);
		}
		return message;
	}

	public RegistryMessage toResponse(ResponseCode code, String message)
	{
		RegistryMessage response = new RegistryMessage();
		response.setProtocol(protocol);
		response.setVersion(version);
		response.setMessageId(messageId);
		response.setCode(code);
		response.setMessage(message);
		response.setCommand(command);
		response.addNameList(nameSet);
		return response;
	}

	public void addItem(RegistryItem item)
	{
		itemSet.add(item);
	}

	public void addItemList(Collection<RegistryItem> itemList)
	{
		if (itemList == null || itemList.isEmpty())
			return;

		itemSet.addAll(itemList);
	}

	public RegistryItem getItem()
	{
		if (itemSet.isEmpty())
			return null;

		return itemSet.iterator().next();
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

	public String getName()
	{
		if (nameSet.isEmpty())
			return null;

		return nameSet.iterator().next();
	}

	public int itemSize()
	{
		return itemSet.size();
	}

	public int nameSet()
	{
		return nameSet.size();
	}

	public void setCommand(short command)
	{
		this.command = RegistryCommand.valueOf(command);
	}

	public RegistryCommand getCommand()
	{
		return command;
	}

	public void setCommand(RegistryCommand command)
	{
		this.command = command;
	}

	public Set<String> getNameSet()
	{
		return nameSet;
	}

	public Set<RegistryItem> getItemSet()
	{
		return itemSet;
	}
}
