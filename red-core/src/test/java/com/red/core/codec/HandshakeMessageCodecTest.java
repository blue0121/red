package com.red.core.codec;

import com.red.core.message.HandshakeMessage;
import com.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HandshakeMessageCodecTest
{
	private ByteBuf buf;
	private HandshakeMessageCodec codec;

	public HandshakeMessageCodecTest()
	{
	}

	@Before
	public void before()
	{
		buf = Unpooled.buffer(100);
		codec = new HandshakeMessageCodec();
	}

	@Test
	public void test1()
	{
		HandshakeMessage message = HandshakeMessage.create("123");
		codec.encode(message, buf);
		Assert.assertEquals(23, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HandshakeMessage message2 = (HandshakeMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(message.getToken(), message2.getToken());
	}

	@Test
	public void test2()
	{
		HandshakeMessage message = HandshakeMessage.create(null);
		codec.encode(message, buf);
		Assert.assertEquals(20, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HandshakeMessage message2 = (HandshakeMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertNull(message2.getToken());
	}

	@Test
	public void test3()
	{
		HandshakeMessage message = HandshakeMessage.create("");
		codec.encode(message, buf);
		Assert.assertEquals(20, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HandshakeMessage message2 = (HandshakeMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertNull(message2.getToken());
	}

}
