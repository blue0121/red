package blue.test.red.core.message;

import blue.red.core.message.HeartbeatMessage;
import blue.red.core.message.Protocol;
import blue.red.core.util.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class HeartbeatMessageTest
{
	public HeartbeatMessageTest()
	{
	}

	@Test
	public void test1()
	{
		HeartbeatMessage ping = HeartbeatMessage.createPing();
		Assertions.assertNotNull(ping);
		Assertions.assertEquals(Protocol.HEARTBEAT, ping.getProtocol());
		Assertions.assertEquals(Constant.DEFAULT_VERSION, ping.getVersion());
		Assertions.assertEquals(HeartbeatMessage.PING, ping.getType());
		Assertions.assertTrue(ping.isPing());

		HeartbeatMessage pong = ping.toPong();
		Assertions.assertNotNull(pong);
		Assertions.assertEquals(Protocol.HEARTBEAT, pong.getProtocol());
		Assertions.assertEquals(Constant.DEFAULT_VERSION, pong.getVersion());
		Assertions.assertEquals(HeartbeatMessage.PONG, pong.getType());
		Assertions.assertTrue(pong.isPong());
		Assertions.assertEquals(ping.getMessageId(), pong.getMessageId());
	}

}
