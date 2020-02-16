package blue.test.red.core.util;

import blue.red.core.message.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertNotNull(ver10);
		Assertions.assertEquals(Version.VER_1_0, ver10);
		Assertions.assertEquals("v1.0", ver10.toString());
		Assertions.assertEquals(1, ver10.major());
		Assertions.assertEquals(0, ver10.minor());

		Version ver11 = Version.valueOf(0x10001);
		Assertions.assertNotNull(ver11);
		Assertions.assertEquals(Version.VER_1_1, ver11);
		Assertions.assertEquals("v1.1", ver11.toString());
		Assertions.assertEquals(1, ver11.major());
		Assertions.assertEquals(1, ver11.minor());
		Assertions.assertEquals(Version.VER_1_0, ver11.majorVersion());

	}

}
