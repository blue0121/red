package com.red.client;

import com.red.client.net.NettyConnectionClient;
import com.red.core.message.Message;
import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public class ClientMain
{
	private static Logger logger = LoggerFactory.getLogger(ClientMain.class);

	public static final int TIMEOUT = 5000;
	public static final String TOKEN = "token";
	public static final String ADDRESS = "localhost:7903";

	public ClientMain()
	{
	}

	public static void main(String[] args) throws Exception
	{
		NettyConnectionClient client = new NettyConnectionClient(TIMEOUT, TOKEN, ADDRESS);
		try
		{
			client.start();
			registry(client);

			Thread.sleep(15_000);
		}
		finally
		{
			client.stop();
		}
	}

	private static void registry(NettyConnectionClient client) throws Exception
	{
		RegistryMessage request = RegistryMessage.create(RegistryCommand.SAVE, "blue");
		request.addItem("localhost:8080");
		Future<Message> future = client.sendMessage(request, r ->
		{
			logger.info("Response: {}, 0x{}", r.getProtocol(), Long.toHexString(r.getMessageId()));
		});
		RegistryMessage response = (RegistryMessage) future.get(10, TimeUnit.SECONDS);
		logger.info("Response: {}, 0x{}, name: {}, items: {}", response.getProtocol(), Long.toHexString(response.getMessageId()),
				response.getName(), response.getItemList());

		request = RegistryMessage.create(RegistryCommand.LIST, "blue");
		future = client.sendMessage(request);
		response = (RegistryMessage) future.get(10, TimeUnit.SECONDS);
		logger.info("Response: {}, 0x{}, name: {}, items: {}", response.getProtocol(), Long.toHexString(response.getMessageId()),
				response.getName(), response.getItemList());
	}

}
