package com.red.server.config;

/**
 * @author Jin Zheng
 * @since 2019-06-07
 */
public class RedConfigItem
{
	public static final String PORT = "port";
	public static final int PORT_VALUE = 7903;
	public static final String TOKEN = "token";
	public static final String TOKEN_VALUE = "token";

	public static final String NETTY_THREAD = "netty.thread";
	public static final int NETTY_THREAD_VALUE = Runtime.getRuntime().availableProcessors() * 2;

	public static final String SESSION_HEARTBEAT = "session.heartbeat";
	public static final int SESSION_HEARTBEAT_VALUE = 3;
	public static final String SESSION_TIMEOUT = "session.timeout";
	public static final int SESSION_TIMEOUT_VALUE = 10;

	public static final String CACHE_MEMORY_MAX_SIZE = "cache.memory.max_size";
	public static final int CACHE_MEMORY_MAX_SIZE_VALUE = 10_000_000;
	public static final String CACHE_MEMORY_PARTITION = "cache.memory.partition";
	public static final int CACHE_MEMORY_PARTITION_VALUE = 4;


	private RedConfigItem()
	{
	}

}
