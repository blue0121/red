package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.RegistryItem;
import com.red.core.message.RegistryMessage;
import io.netty.buffer.ByteBuf;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2019-05-04
 */
public class RegistryMessageCodec extends ResponseMessageCodec
{
	public RegistryMessageCodec()
	{
	}

	@Override
	protected void encodeBody(Message message, ByteBuf out)
	{
		super.encodeBody(message, out);
		RegistryMessage registry = (RegistryMessage) message;
		out.writeShort(registry.getCommand().value());

		Set<String> nameSet = registry.getNameSet();
		out.writeShort(nameSet.size());
		for (String name : nameSet)
		{
			this.writeString(name, out);
		}

		Set<RegistryItem> itemSet = registry.getItemSet();
		out.writeShort(itemSet.size());
		for (RegistryItem item : itemSet)
		{
			this.writeString(item.getItem(), out);
			this.writeString(item.getToken(), out);
		}
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		super.decodeBody(message, in);
		RegistryMessage registry = (RegistryMessage) message;
		registry.setCommand(in.readShort());

		short nameSize = in.readShort();
		for (short i = 0; i < nameSize; i++)
		{
			registry.addName(this.readString(in));
		}

		short itemSize = in.readShort();
		for (short i = 0; i < itemSize; i++)
		{
			String item = this.readString(in);
			String token = this.readString(in);
			registry.addItem(new RegistryItem(item, token));
		}
	}

	@Override
	public Message getMessage()
	{
		return new RegistryMessage();
	}
}
