package blue.test.red.client;

import blue.red.client.ConnectionListener;
import blue.red.client.RedClientException;
import blue.red.client.config.RedConfig;
import blue.test.red.client.registry.RegistryClientTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class ClientMain
{
	private static Logger logger = LoggerFactory.getLogger(ClientMain.class);

	public static final RedConfig RED_CONFIG = new RedConfig();
	public static final String ADDRESS = "localhost:7903";

	public ClientMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ConnectionListener listener = new ConnectionListenerTest();
		RegistryClientTest client1 = new RegistryClientTest(ADDRESS, RED_CONFIG, listener);
		RegistryClientTest client2 = new RegistryClientTest(ADDRESS, RED_CONFIG, listener);

		try
		{
			save(client1, client2);
		}
		catch (RedClientException e)
		{
			logger.error("Error", e);
		}

		Thread.sleep(10000);
		client2.close();
		client1.close();
	}

	private static void save2(RegistryClientTest client1)
	{
		client1.save("blue");
	}

	private static void save(RegistryClientTest client1, RegistryClientTest client2) throws InterruptedException
	{
		client1.bind("127.0.0.1", 10000);
		client2.bind("127.0.0.2", 20000);

		client1.watch("blue1");
		client2.watch("red1");

		client1.save("blue1", "blue2", "blue3", "blue4", "blue5");
		client2.save("red1", "red2", "red3", "red4", "red5");

		Thread.sleep(500);

		client1.save("red1");
		client2.save("blue1");

	}


}
