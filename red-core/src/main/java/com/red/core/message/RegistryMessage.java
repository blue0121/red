package com.red.core.message;

import com.red.core.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2019-05-04
 */
public class RegistryMessage extends Response
{
	private String name;
	private List<String> itemList = new ArrayList<>();

	public RegistryMessage()
	{
	}

	public static RegistryMessage create(String name)
	{
		RegistryMessage message = new RegistryMessage();
		message.setProtocol(Protocol.REGISTRY);
		message.setVersion(Constant.DEFAULT_VERSION);
		message.setMessageId(SingleSnowflakeId.getInstance().nextId());
		message.setCode(ResponseCode.SUCCESS);
		message.setName(name);
		return message;
	}

	public void addItem(String item)
	{
		itemList.add(item);
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
