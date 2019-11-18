package blue.test.red.core.codec;

import blue.red.core.codec.CacheMessageCodec;
import blue.red.core.message.CacheCommand;
import blue.red.core.message.CacheMessage;
import blue.red.core.message.Protocol;
import blue.red.core.message.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 2019-07-07
 */
public class CacheMessageCodecTest
{
	private ByteBuf buf;
	private CacheMessageCodec codec;

	public CacheMessageCodecTest()
	{
	}

	@Before
	public void before()
	{
		buf = Unpooled.buffer(100);
		codec = new CacheMessageCodec();
	}

	@Test
	public void test1()
	{
		String key = "blue";
		long ttl = 10L;
		CacheMessage message = CacheMessage.createPersistence(CacheCommand.GET, key);
		message.setCompress(true);
		message.setValue(key.getBytes());
		message.setTtl(ttl);
		codec.encode(message, buf);

		Protocol protocol = Protocol.valueOf(buf.readInt());
		CacheMessage message2 = (CacheMessage) codec.decode(protocol, buf);
		Assert.assertNotNull(message2);
		Assert.assertEquals(message.getProtocol(), message2.getProtocol());
		Assert.assertEquals(message.getVersion(), message2.getVersion());
		Assert.assertEquals(message.getMessageId(), message2.getMessageId());
		Assert.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assert.assertNull(message2.getMessage());
		Assert.assertEquals(CacheMessage.PERSISTENCE, message2.getState());
		Assert.assertEquals(message.isCompress(), message2.isCompress());
		Assert.assertEquals(key, message2.getKey());
		Assert.assertArrayEquals(key.getBytes(), message2.getValue());
		Assert.assertEquals(ttl, message2.getTtl());
	}

}
