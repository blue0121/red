package com.red.core.codec;

import com.red.core.message.Protocol;
import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
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

	@Before
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
		Assert.assertEquals(38, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assert.assertNull(message2.getMessage());
		Assert.assertEquals("blue", message2.getName());
		Assert.assertTrue(message2.getItemSet().isEmpty());
		Assert.assertNull(message2.getItem());
	}

	@Test
	public void test2()
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.SAVE, "blue");
		message.addItem("127.0.0.1:8080");
		message.addItem("127.0.0.1:8081");
		codec.encode(message, buf);
		Assert.assertEquals(74, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assert.assertNull(message2.getMessage());

		Set<String> itemSet = new HashSet<>();
		itemSet.add("127.0.0.1:8080");
		itemSet.add("127.0.0.1:8081");
		Assert.assertEquals(itemSet, message2.getItemSet());
	}

	@Test
	public void test3()
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.SAVE, "blue", "red2");
		message.addItem("127.0.0.1:8080");
		message.addItem("127.0.0.1:8081");
		codec.encode(message, buf);
		Assert.assertEquals(82, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assert.assertNull(message2.getMessage());

		Set<String> itemSet = new HashSet<>();
		itemSet.add("127.0.0.1:8080");
		itemSet.add("127.0.0.1:8081");
		Assert.assertEquals(itemSet, message2.getItemSet());

		Set<String> nameSet = new HashSet<>();
		nameSet.add("blue");
		nameSet.add("red2");
		Assert.assertEquals(nameSet, message2.getNameSet());
	}

}
