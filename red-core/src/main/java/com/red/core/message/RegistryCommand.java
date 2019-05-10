package com.red.core.message;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-05
 */
public enum RegistryCommand
{
	SAVE((short)0x1),
	DELETE((short)0x2),
	LIST((short)0x3),
	WATCH((short)0x11);

	private short value;

	RegistryCommand(short value)
	{
		this.value = value;
	}

	public short value()
	{
		return value;
	}

	public static RegistryCommand valueOf(short value)
	{
		for (RegistryCommand cmd : RegistryCommand.values())
		{
			if (cmd.value == value)
				return cmd;
		}
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("RegistryCommand[0x%x: %s]", value, this.name());
	}
}
