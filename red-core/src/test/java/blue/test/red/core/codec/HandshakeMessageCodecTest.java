package blue.test.red.core.codec;

import blue.red.core.codec.HandshakeMessageCodec;
import blue.red.core.message.HandshakeMessage;
import blue.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
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
		Assertions.assertEquals(23, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HandshakeMessage message2 = (HandshakeMessage) codec.decode(protocol, buf);
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertEquals(message.getToken(), message2.getToken());
	}

	@Test
	public void test2()
	{
		HandshakeMessage message = HandshakeMessage.create(null);
		codec.encode(message, buf);
		Assertions.assertEquals(20, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HandshakeMessage message2 = (HandshakeMessage) codec.decode(protocol, buf);
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertNull(message2.getToken());
	}

	@Test
	public void test3()
	{
		HandshakeMessage message = HandshakeMessage.create("");
		codec.encode(message, buf);
		Assertions.assertEquals(20, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HandshakeMessage message2 = (HandshakeMessage) codec.decode(protocol, buf);
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertNull(message2.getToken());
	}

}
