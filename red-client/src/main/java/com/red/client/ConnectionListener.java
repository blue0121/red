package com.red.client;

import java.net.InetSocketAddress;

/**
 * @author Jin Zheng
 * @since 2019-06-01
 */
public interface ConnectionListener
{

	void connected(InetSocketAddress address);

	void disconnected(InetSocketAddress address);

}
