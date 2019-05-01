package com.red.core.message;

import com.red.core.util.Constant;
import com.red.core.util.Protocol;
import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertNotNull(ping);
		Assert.assertEquals(Protocol.HEARTBEAT, ping.getProtocol());
		Assert.assertEquals(Constant.DEFAULT_VERSION, ping.getVersion());
		Assert.assertEquals(HeartbeatMessage.PING, ping.getType());
		Assert.assertTrue(ping.isPing());

		HeartbeatMessage pong = ping.toPong();
		Assert.assertNotNull(pong);
		Assert.assertEquals(Protocol.HEARTBEAT, pong.getProtocol());
		Assert.assertEquals(Constant.DEFAULT_VERSION, pong.getVersion());
		Assert.assertEquals(HeartbeatMessage.PONG, pong.getType());
		Assert.assertTrue(pong.isPong());
		Assert.assertEquals(ping.getMessageId(), pong.getMessageId());
	}

}
