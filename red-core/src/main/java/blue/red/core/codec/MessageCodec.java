package blue.red.core.codec;

import blue.red.core.message.Message;
import blue.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public interface MessageCodec
{
	int LENGTH = 4;
	int ZERO_LENGTH = 0;

	void encode(Message message, ByteBuf out);

	Message decode(Protocol protocol, ByteBuf in);

	Message getMessage();

}
