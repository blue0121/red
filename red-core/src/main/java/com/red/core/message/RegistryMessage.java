package com.red.core.message;

import com.red.core.util.Constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-05-04
 */
public class RegistryMessage extends Response
{
	private RegistryCommand command;
	private String name;
	private List<String> itemList = new ArrayList<>();

	public RegistryMessage()
	{
	}

	public static RegistryMessage create(RegistryCommand command, String name)
	{
		RegistryMessage message = new RegistryMessage();
		message.setProtocol(Protocol.REGISTRY);
		message.setVersion(Constant.DEFAULT_VERSION);
		message.setMessageId(SingleSnowflakeId.getInstance().nextId());
		message.setCode(ResponseCode.SUCCESS);
		message.setCommand(command);
		message.setName(name);
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
		response.setName(name);
		return response;
	}

	public void addItem(String item)
	{
		if (!itemList.contains(item))
		{
			itemList.add(item);
		}
	}

	public void addItemList(Collection<String> itemList)
	{
		if (itemList == null || itemList.isEmpty())
			return;

		for (String item : itemList)
		{
			this.itemList.add(item);
		}
	}

	public String getItem()
	{
		if (itemList.isEmpty())
			return null;

		return itemList.get(0);
	}

	public int itemSize()
	{
		return itemList.size();
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<String> getItemList()
	{
		return itemList;
	}
}
