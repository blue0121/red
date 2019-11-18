package blue.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public enum  ResponseCode
{
	SUCCESS(0x0),
	ERROR(0x1),
	HANDSHAKE(0x2),
	REGISTRY(0x11),
	CACHE(0x12);

	private int code;

	ResponseCode(int code)
	{
		this.code = code;
	}

	public static ResponseCode valueOf(int code)
	{
		for (ResponseCode responseCode : ResponseCode.values())
		{
			if (responseCode.code == code)
				return responseCode;
		}
		return null;
	}

	public int code()
	{
		return this.code;
	}

}
