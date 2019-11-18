package blue.red.core.message;

/**
 * @author Jin Zheng
 * @since 2019-05-01
 */
public class Response extends Message
{
	protected ResponseCode code;
	protected String message;

	public Response()
	{
	}

	public static Response from(Message request, ResponseCode code, String message)
	{
		Response response = new Response();
		response.setProtocol(request.getProtocol());
		response.setVersion(request.getVersion());
		response.setMessageId(request.getMessageId());
		response.setCode(code);
		response.setMessage(message);
		return response;
	}

	public void setCode(int code)
	{
		this.code = ResponseCode.valueOf(code);
	}

	public ResponseCode getCode()
	{
		return code;
	}

	public void setCode(ResponseCode code)
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
