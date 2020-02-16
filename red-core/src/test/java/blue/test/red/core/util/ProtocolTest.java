package blue.test.red.core.util;

import blue.red.core.message.Protocol;
import blue.red.core.util.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class ProtocolTest
{
	public ProtocolTest()
	{
	}

	@Test
	public void test1()
	{
		Protocol handshake = Protocol.valueOf(0x1);
		Assertions.assertNotNull(handshake);
		Assertions.assertEquals(Protocol.HANDSHAKE, handshake);
		Assertions.assertEquals(Constant.PROTOCOL | 0x1, handshake.value());
		Assertions.assertEquals(0x1, handshake.originalValue());
	}

}
