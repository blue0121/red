package com.red.client.registry;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class Host
{
	private String ip;
	private int port;

	public Host()
	{
	}

	public Host(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder(32);
		str.append(ip).append(":").append(port);
		return str.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Host host = (Host) o;
		return port == host.port &&
				Objects.equals(ip, host.ip);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ip, port);
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
