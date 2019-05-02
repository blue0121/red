package com.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public enum  ResponseCode
{
	SUCCESS(0),
	ERROR(1),
	HANDSHAKE(2);

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
