package blue.red.server;

import blue.red.server.config.RedConfig;
import blue.red.server.net.Server;

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
		RedConfig.getInstance();
		Server server = new Server();
		server.start();
	}

}
