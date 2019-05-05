package com.red.core.codec;

import com.red.core.message.Protocol;
import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
		buf = Unpooled.buffer(100);
		codec = new RegistryMessageCodec();
	}

	@Test
	public void test1()
	{
		RegistryMessage message = RegistryMessage.create("blue");
		codec.encode(message, buf);
		Assert.assertEquals(36, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assert.assertNull(message2.getMessage());
		Assert.assertEquals("blue", message2.getName());
		Assert.assertTrue(message2.getItemList().isEmpty());
		Assert.assertNull(message2.getItem());
	}

	@Test
	public void test2()
	{
		RegistryMessage message = RegistryMessage.create("blue");
		message.addItem("127.0.0.1:8080");
		message.addItem("127.0.0.1:8081");
		codec.encode(message, buf);
		Assert.assertEquals(72, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		RegistryMessage message2 = (RegistryMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assert.assertNull(message2.getMessage());
		Assert.assertEquals("blue", message2.getName());
		Assert.assertEquals(2, message2.getItemList().size());
		String item0 = message2.getItem();
		Assert.assertEquals("127.0.0.1:8080", item0);
		String item1 = message2.getItemList().get(1);
		Assert.assertEquals("127.0.0.1:8081", item1);
	}

}
