package com.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class Response extends Message
{
	protected int code;
	protected String message;

	public Response()
	{
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
