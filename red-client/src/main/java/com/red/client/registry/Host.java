package com.red.client.registry;

import java.util.Objects;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public class Host
{
	public static final String SPLIT = ":";

	private String ip;
	private int port;
	private String token;

	public Host()
	{
	}

	public Host(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}

	/**
	 * parse string: host:ip
	 * @param strHost
	 * @return
	 */
	public static Host parse(String strHost)
	{
		String[] str = strHost.split(SPLIT);
		return new Host(str[0], Integer.parseInt(str[1]));
	}

	/**
	 * to string: host:ip
	 * @return
	 */
	public String toAddr()
	{
		return String.format("%s:%d", ip, port);
	}

	@Override
	public String toString()
	{
		return String.format("%s:%d", ip, port);
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

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
