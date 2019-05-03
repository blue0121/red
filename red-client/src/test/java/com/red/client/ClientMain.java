package com.red.client;

import com.red.client.net.NettyConnectionClient;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class ClientMain
{
	public static final int TIMEOUT = 5000;
	public static final String TOKEN = "token";
	public static final String ADDRESS = "localhost:7903";

	public ClientMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ConnectionClient client = new NettyConnectionClient(TIMEOUT, TOKEN, ADDRESS);
		client.start();

		Thread.sleep(50_000);
		client.stop();
	}

}
