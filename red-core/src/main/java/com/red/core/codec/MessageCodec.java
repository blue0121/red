package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.Protocol;
import io.netty.buffer.ByteBuf;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public interface MessageCodec
{

	void encode(Message message, ByteBuf out);

	Message decode(Protocol protocol, ByteBuf in);

	Message getMessage();

}
