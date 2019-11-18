package blue.red.core.util;

import blue.red.core.message.Version;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class Constant
{
	// protocal
	public static final int PROTOCOL = 0x01210000;
	public static final int PROTOCOL_MASK = 0xFFFF;

	// version
	public static final int MAJOR_VERSION_MASK = 0xFFFF0000;
	public static final int MINOR_VERSION_MASK = 0xFFFF;
	public static final Version DEFAULT_VERSION = Version.VER_1_0;

	// compress
	public static final int COMPRESS_SIZE = 200;


	private Constant()
	{
	}

}
