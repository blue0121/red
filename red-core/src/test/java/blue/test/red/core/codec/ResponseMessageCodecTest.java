package blue.test.red.core.codec;

import blue.red.core.codec.ResponseMessageCodec;
import blue.red.core.message.HandshakeMessage;
import blue.red.core.message.Protocol;
import blue.red.core.message.Response;
import blue.red.core.message.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
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
		Assertions.assertEquals(24, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		Response response2 = (Response) codec.decode(protocol, buf);
		Assertions.assertEquals(response.getProtocol(), response2.getProtocol());
		Assertions.assertEquals(response.getVersion(), response2.getVersion());
		Assertions.assertEquals(response.getMessageId(), response2.getMessageId());
		Assertions.assertEquals(response.getCode(), response2.getCode());
		Assertions.assertNull(response2.getMessage());
	}

	@Test
	public void test2()
	{
		Response response = Response.from(request, ResponseCode.HANDSHAKE, "Handshake error");
		codec.encode(response, buf);
		Assertions.assertEquals(39, buf.readableBytes());

		Protocol protocol = Protocol.valueOf(buf.readInt());
		Response response2 = (Response) codec.decode(protocol, buf);
		Assertions.assertEquals(response.getProtocol(), response2.getProtocol());
		Assertions.assertEquals(response.getVersion(), response2.getVersion());
		Assertions.assertEquals(response.getMessageId(), response2.getMessageId());
		Assertions.assertEquals(response.getCode(), response2.getCode());
		Assertions.assertEquals(response.getMessage(), response2.getMessage());
	}

}
