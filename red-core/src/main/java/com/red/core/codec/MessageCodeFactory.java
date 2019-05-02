package com.red.core.codec;

import com.red.core.message.Protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-05-02
 */
public class MessageCodeFactory
{
	private Map<Protocol, MessageCodec> clientEncoderMap = new HashMap<>();
	private Map<Protocol, MessageCodec> clientDecoderMap = new HashMap<>();
	private Map<Protocol, MessageCodec> serverEncoderMap = new HashMap<>();
	private Map<Protocol, MessageCodec> serverDecoderMap = new HashMap<>();

	private static MessageCodeFactory factory;

	private MessageCodeFactory()
	{
		this.initClientEncoder();
		this.initClientDecoder();
		this.initServerEncoder();
		this.initServerDecoder();
	}

	public static MessageCodeFactory getFactory()
	{
		if (factory == null)
		{
			synchronized (MessageCodeFactory.class)
			{
				if (factory == null)
				{
					factory = new MessageCodeFactory();
				}
			}
		}
		return factory;
	}

	public Map<Protocol, MessageCodec> getClientEncoderMap()
	{
		return clientEncoderMap;
	}

	public Map<Protocol, MessageCodec> getClientDecoderMap()
	{
		return clientDecoderMap;
	}

	public Map<Protocol, MessageCodec> getServerEncoderMap()
	{
		return serverEncoderMap;
	}

	public Map<Protocol, MessageCodec> getServerDecoderMap()
	{
		return serverDecoderMap;
	}

	private void initClientEncoder()
	{
		clientEncoderMap.put(Protocol.HANDSHAKE, new HandshakeMessageCodec());
		clientEncoderMap.put(Protocol.HEARTBEAT, new HeartbeatMessageCodec());
	}

	private void initClientDecoder()
	{
		clientDecoderMap.put(Protocol.HANDSHAKE, new ResponseMessageCodec());
		clientDecoderMap.put(Protocol.HEARTBEAT, new HeartbeatMessageCodec());
	}

	private void initServerEncoder()
	{
		serverEncoderMap.put(Protocol.HANDSHAKE, new ResponseMessageCodec());
		serverEncoderMap.put(Protocol.HEARTBEAT, new HeartbeatMessageCodec());
	}

	private void initServerDecoder()
	{
		serverEncoderMap.put(Protocol.HANDSHAKE, new HandshakeMessageCodec());
		serverDecoderMap.put(Protocol.HEARTBEAT, new HeartbeatMessageCodec());
	}

}
