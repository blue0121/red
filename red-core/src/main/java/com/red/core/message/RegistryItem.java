package com.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-04
 */
public class RegistryItem
{
	private String ip;
	private int port;

	public RegistryItem()
	{
	}

	public static RegistryItem create(String ip, int port)
	{
		RegistryItem item = new RegistryItem();
		item.ip = ip;
		item.port = port;
		return item;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}
}
