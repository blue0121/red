package blue.test.red.core.codec;

import blue.red.core.codec.ResponseMessageCodec;
import blue.red.core.message.HandshakeMessage;
import blue.red.core.message.Protocol;
import blue.red.core.message.Response;
import blue.red.core.message.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class ResponseMessageCodecTest
{
	private ByteBuf buf;
	private ResponseMessageCodec codec;
	private HandshakeMessage request;

	public ResponseMessageCodecTest()
	{
	}

	@Before
	public void before()
	{
		buf = Unpooled.buffer(100);
		codec = new ResponseMessageCodec();
		request = HandshakeMessage.create(null);
	}

	@Test
	public void test1()
	{
		Response response = Response.from(request, ResponseCode.SUCCESS, null);
		codec.encode(response, buf);
		Assert.assertEquals(24, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		Response response2 = (Response) codec.decode(protocol, buf);
		Assert.assertEquals(response.getProtocol(), response2.getProtocol());
		Assert.assertEquals(response.getVersion(), response2.getVersion());
		Assert.assertEquals(response.getMessageId(), response2.getMessageId());
		Assert.assertEquals(response.getCode(), response2.getCode());
		Assert.assertNull(response2.getMessage());
	}

	@Test
	public void test2()
	{
		Response response = Response.from(request, ResponseCode.HANDSHAKE, "Handshake error");
		codec.encode(response, buf);
		Assert.assertEquals(39, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		Response response2 = (Response) codec.decode(protocol, buf);
		Assert.assertEquals(response.getProtocol(), response2.getProtocol());
		Assert.assertEquals(response.getVersion(), response2.getVersion());
		Assert.assertEquals(response.getMessageId(), response2.getMessageId());
		Assert.assertEquals(response.getCode(), response2.getCode());
		Assert.assertEquals(response.getMessage(), response2.getMessage());
	}

}
