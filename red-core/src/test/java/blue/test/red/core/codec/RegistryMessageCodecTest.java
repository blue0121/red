package blue.test.red.core.codec;

import blue.red.core.codec.RegistryMessageCodec;
import blue.red.core.message.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-05-04
 */
public class RegistryMessageCodecTest
{
	private ByteBuf buf;
	private RegistryMessageCodec codec;

	public RegistryMessageCodecTest()
	{
	}

	@BeforeEach
	public void before()
	{
		buf = Unpooled.buffer(200);
		codec = new RegistryMessageCodec();
	}

	@Test
	public void test1()
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.LIST, "blue");
		codec.encode(message, buf);
		Assertions.assertTrue(buf.readableBytes() > 0);

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assertions.assertNull(message2.getMessage());
		Assertions.assertEquals("blue", message2.getName());
		Assertions.assertTrue(message2.getItemSet().isEmpty());
		Assertions.assertNull(message2.getItem());
	}

	@Test
	public void test2()
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.SAVE, "blue");
		message.addItem(new RegistryItem("127.0.0.1:8080", "token1"));
		message.addItem(new RegistryItem("127.0.0.1:8081", "token2"));
		codec.encode(message, buf);
		Assertions.assertTrue(buf.readableBytes() > 0);

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assertions.assertNull(message2.getMessage());

		Map<String, String> map = new HashMap<>();
		map.put("127.0.0.1:8080", "token1");
		map.put("127.0.0.1:8081", "token2");
		for (RegistryItem item : message2.getItemSet())
		{
			Assertions.assertEquals(map.get(item.getItem()), item.getToken());
		}
	}

	@Test
	public void test3()
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.SAVE, "blue", "red2");
		message.addItem(new RegistryItem("127.0.0.1:8080", "token1"));
		message.addItem(new RegistryItem("127.0.0.1:8081", "token2"));
		codec.encode(message, buf);
		Assertions.assertTrue(buf.readableBytes() > 0);

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assertions.assertNull(message2.getMessage());

		Map<String, String> map = new HashMap<>();
		map.put("127.0.0.1:8080", "token1");
		map.put("127.0.0.1:8081", "token2");
		for (RegistryItem item : message2.getItemSet())
		{
			Assertions.assertEquals(map.get(item.getItem()), item.getToken());
		}

		Set<String> nameSet = new HashSet<>();
		nameSet.add("blue");
		nameSet.add("red2");
		Assertions.assertEquals(nameSet, message2.getNameSet());
	}

}
