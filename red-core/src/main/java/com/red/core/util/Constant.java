package com.red.core.util;

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


	private Constant()
	{
	}

}
