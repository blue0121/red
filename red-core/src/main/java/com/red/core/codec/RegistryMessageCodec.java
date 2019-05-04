package com.red.core.codec;

import com.red.core.message.Message;
import com.red.core.message.RegistryItem;
import com.red.core.message.RegistryMessage;
import io.netty.buffer.ByteBuf;

import java.util.List;

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
		this.writeString(registry.getName(), out);
		List<RegistryItem> itemList = registry.getRegistryItemList();
		out.writeInt(itemList.size());
		for (RegistryItem item : itemList)
		{
			this.writeString(item.getIp(), out);
			out.writeInt(item.getPort());
		}
	}

	@Override
	protected void decodeBody(Message message, ByteBuf in)
	{
		super.decodeBody(message, in);
		RegistryMessage registry = (RegistryMessage) message;
		registry.setName(this.readString(in));
		int size = in.readInt();
		for (int i = 0; i < size; i++)
		{
			String ip = this.readString(in);
			int port = in.readInt();
			registry.addRegistryItem(ip, port);
		}
	}

	@Override
	public Message getMessage()
	{
		return new RegistryMessage();
	}
}
