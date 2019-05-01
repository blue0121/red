package com.red.core.util;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public enum Version
{
	VER_1_0(0x10000),
	VER_1_1(0x10001),
	VER_2_0(0x20000),
	VER_2_1(0x20001);

	private int value;

	Version(int value)
	{
		this.value = value;
	}

	public static Version valueOf(int value)
	{
		for (Version version : Version.values())
		{
			if (version.value == value)
				return version;
		}
		return null;
	}

	public Version majorVersion()
	{
		return Version.valueOf(this.value & Constant.MAJOR_VERSION_MASK);
	}

	public int major()
	{
		return (this.value & Constant.MAJOR_VERSION_MASK) >> 16;
	}

	public int minor()
	{
		return this.value & Constant.MINOR_VERSION_MASK;
	}

	public int value()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		StringBuilder version = new StringBuilder(8);
		version.append("v")
					.append((value & Constant.MAJOR_VERSION_MASK) >> 16)
					.append(".")
					.append(value & Constant.MINOR_VERSION_MASK);
		return version.toString();
	}
}
