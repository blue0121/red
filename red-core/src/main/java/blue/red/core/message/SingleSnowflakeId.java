package blue.red.core.message;

/**
 * 单机版的发号器
 *
 * @author zhengjin
 * @since 1.0 2018年08月23日
 */
public class SingleSnowflakeId extends SnowflakeId
{
	private static SingleSnowflakeId instance;

	public static SingleSnowflakeId getInstance()
	{
		if (instance == null)
		{
			synchronized (SingleSnowflakeId.class)
			{
				if (instance == null)
				{
					instance = new SingleSnowflakeId();
				}
			}
		}
		return instance;
	}

	private SingleSnowflakeId()
	{
		super(0L, 12L, 0L);
	}

}
