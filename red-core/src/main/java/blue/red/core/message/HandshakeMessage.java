package blue.red.core.message;

import blue.red.core.util.Constant;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class HandshakeMessage extends Message
{
	private String token;

	public HandshakeMessage()
	{
	}

	public static HandshakeMessage create(String token)
	{
		HandshakeMessage message = new HandshakeMessage();
		message.setProtocol(Protocol.HANDSHAKE);
		message.setVersion(Constant.DEFAULT_VERSION);
		message.setMessageId(SingleSnowflakeId.getInstance().nextId());
		message.setToken(token);
		return message;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
