package com.red.server;

import com.red.server.net.Server;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class ServerMain
{
	public ServerMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		Server server = new Server();
		server.start();
	}

}
