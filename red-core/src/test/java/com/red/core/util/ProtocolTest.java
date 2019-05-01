package com.red.core.util;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertNotNull(handshake);
		Assert.assertEquals(Protocol.HANDSHAKE, handshake);
		Assert.assertEquals(Constant.PROTOCOL | 0x1, handshake.value());
		Assert.assertEquals(0x1, handshake.originalValue());
	}

}
