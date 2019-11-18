package blue.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-03
 */
public enum CacheCommand
{
	GET((short)0x1),
	SET((short)0x2),
	DELETE((short)0x3);

	private short value;

	CacheCommand(short value)
	{
		this.value = value;
	}

	public short value()
	{
		return value;
	}

	public static CacheCommand valueOf(short value)
	{
		for (CacheCommand cmd : CacheCommand.values())
		{
			if (cmd.value == value)
				return cmd;
		}
		return null;
	}

}
