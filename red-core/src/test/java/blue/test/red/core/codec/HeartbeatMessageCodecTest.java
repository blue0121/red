package blue.test.red.core.codec;

import blue.red.core.codec.HeartbeatMessageCodec;
import blue.red.core.message.HeartbeatMessage;
import blue.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HeartbeatMessageCodecTest
{
	private ByteBuf buf;
	private HeartbeatMessageCodec codec;

	public HeartbeatMessageCodecTest()
	{
	}

	@Before
	public void before()
	{
		buf = Unpooled.buffer(100);
		codec = new HeartbeatMessageCodec();
	}

	@Test
	public void test1()
	{
		HeartbeatMessage message = HeartbeatMessage.createPing();
		codec.encode(message, buf);
		Assert.assertEquals(17, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		HeartbeatMessage message2 = (HeartbeatMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(message.getType(), message2.getType());
	}

}
