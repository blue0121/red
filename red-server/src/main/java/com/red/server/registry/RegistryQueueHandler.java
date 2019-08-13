package com.red.server.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import com.red.core.message.ResponseCode;
import com.red.server.common.RedServerException;
import com.red.server.queue.MessageChannel;
import com.red.server.queue.QueueHandler;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RegistryQueueHandler implements QueueHandler<RegistryMessage>
{
    private static Logger logger = LoggerFactory.getLogger(RegistryQueueHandler.class);

    private final Map<RegistryCommand, RegistryHandler> handlerMap = new HashMap<>();
    private final RegistryChannelGroup channelGroup;
    private final RegistryStorage registryStorage;


    public RegistryQueueHandler()
    {
        this.channelGroup = new RegistryChannelGroup();
        this.registryStorage = new MemoryRegistryStorage(channelGroup);

        handlerMap.put(RegistryCommand.SAVE, new SaveRegistryHandler(registryStorage));
        handlerMap.put(RegistryCommand.DELETE, new DeleteRegistryHandler(registryStorage));
        handlerMap.put(RegistryCommand.LIST, new ListRegistryHandler(registryStorage));
        handlerMap.put(RegistryCommand.WATCH, new WatchRegistryHandler(channelGroup));
        handlerMap.put(RegistryCommand.UNWATCH, new UnwatchRegistryHandler(channelGroup));
        handlerMap.put(RegistryCommand.BIND, new BindRegistryHandler(channelGroup));
        handlerMap.put(RegistryCommand.UNBIND, new UnbindRegistryHandler(registryStorage));
    }

    @Override
    public void handle(MessageChannel<RegistryMessage> data)
    {
        RegistryMessage message = data.getMessage();
        Channel channel = data.getChannel();
        RegistryHandler handler = handlerMap.get(message.getCommand());
        if (handler == null)
        {
            logger.error("Missing RegistryHandler: {}", message.getCommand());
            return;
        }
        ResponseCode code = ResponseCode.SUCCESS;
        String msg = "Successful";
        boolean error = true;
        try
        {
            handler.handle(message, channel);
            error = false;
        }
        catch (RedServerException e)
        {
            code = ResponseCode.REGISTRY;
            msg = e.getMessage();
        }
        catch (Throwable e)
        {
            code = ResponseCode.ERROR;
            msg = "Unknown";
            logger.error("Error: ", e);
        }
        if (error)
        {
            RegistryMessage response = message.toResponse(code, msg);
            channel.writeAndFlush(response);
        }
    }
}