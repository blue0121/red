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
	private List<RegistryItem> registryItemList = new ArrayList<>();

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

	public void addRegistryItem(RegistryItem item)
	{
		registryItemList.add(item);
	}

	public void addRegistryItem(String ip, int port)
	{
		this.addRegistryItem(RegistryItem.create(ip, port));
	}

	public RegistryItem getRegistryItem()
	{
		if (registryItemList == null || registryItemList.isEmpty())
			return null;

		return registryItemList.get(0);
	}

	public int registryItemSize()
	{
		return registryItemList.size();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<RegistryItem> getRegistryItemList()
	{
		return registryItemList;
	}
}
