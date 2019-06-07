package com.red.client.config;

/**
 * @author Jin Zheng
 * @since 2019-06-07
 */
public class RedConfig
{
	public static final int MILLS = 1000;

	private String token = "token";
	private int nettyThread = 2;
	private int sessionHeartbeat = 3;
	private int sessionTimeout = 10;
	private int connectTimeout = 5;
	private int reconnect = 5;

	public RedConfig()
	{
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public int getNettyThread()
	{
		return nettyThread;
	}

	public void setNettyThread(int nettyThread)
	{
		this.nettyThread = nettyThread;
	}

	public int getSessionHeartbeat()
	{
		return sessionHeartbeat;
	}

	public void setSessionHeartbeat(int sessionHeartbeat)
	{
		this.sessionHeartbeat = sessionHeartbeat;
	}

	public int getSessionTimeout()
	{
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout)
	{
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectTimeout()
	{
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	public int getReconnect()
	{
		return reconnect;
	}

	public void setReconnect(int reconnect)
	{
		this.reconnect = reconnect;
	}
}
