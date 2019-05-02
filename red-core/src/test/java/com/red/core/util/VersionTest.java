package com.red.core.util;

import com.red.core.message.Version;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class VersionTest
{
	public VersionTest()
	{
	}

	@Test
	public void test1()
	{
		Version ver10 = Version.valueOf(0x10000);
		Assert.assertNotNull(ver10);
		Assert.assertEquals(Version.VER_1_0, ver10);
		Assert.assertEquals("v1.0", ver10.toString());
		Assert.assertEquals(1, ver10.major());
		Assert.assertEquals(0, ver10.minor());

		Version ver11 = Version.valueOf(0x10001);
		Assert.assertNotNull(ver11);
		Assert.assertEquals(Version.VER_1_1, ver11);
		Assert.assertEquals("v1.1", ver11.toString());
		Assert.assertEquals(1, ver11.major());
		Assert.assertEquals(1, ver11.minor());
		Assert.assertEquals(Version.VER_1_0, ver11.majorVersion());

	}

}
