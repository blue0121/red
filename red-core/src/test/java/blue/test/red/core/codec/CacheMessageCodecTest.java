package blue.test.red.core.codec;

import blue.red.core.codec.CacheMessageCodec;
import blue.red.core.message.CacheCommand;
import blue.red.core.message.CacheMessage;
import blue.red.core.message.Protocol;
import blue.red.core.message.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
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
		Assertions.assertNotNull(message2);
		Assertions.assertEquals(message.getProtocol(), message2.getProtocol());
		Assertions.assertEquals(message.getVersion(), message2.getVersion());
		Assertions.assertEquals(message.getMessageId(), message2.getMessageId());
		Assertions.assertEquals(ResponseCode.SUCCESS, message2.getCode());
		Assertions.assertNull(message2.getMessage());
		Assertions.assertEquals(CacheMessage.PERSISTENCE, message2.getState());
		Assertions.assertEquals(message.isCompress(), message2.isCompress());
		Assertions.assertEquals(key, message2.getKey());
		Assertions.assertArrayEquals(key.getBytes(), message2.getValue());
		Assertions.assertEquals(ttl, message2.getTtl());
	}

}
