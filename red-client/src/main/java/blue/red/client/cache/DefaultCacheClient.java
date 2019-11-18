package blue.red.client.cache;

import blue.red.client.HandlerClient;
import blue.red.client.RedClientException;
import blue.red.client.RedFuture;
import blue.red.client.registry.RegistryClientException;
import blue.red.core.message.CacheCommand;
import blue.red.core.message.CacheMessage;
import blue.red.core.message.Message;
import blue.red.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-13
 */
public class DefaultCacheClient implements CacheClient
{
    private final HandlerClient handlerClient;
    private final CacheMessageListener callbackClient;

    public DefaultCacheClient(HandlerClient handlerClient)
    {
        this.handlerClient = handlerClient;
        this.callbackClient = new CacheMessageListener();
        this.handlerClient.setCacheListener(callbackClient);
    }

    @Override
    public void setSync(CacheInstance instance)
    {
        this.invokeSync(CacheCommand.SET, instance);
    }

    @Override
    public void setAsync(CacheInstance instance, CacheCallback callback)
    {
        this.invokeAsync(CacheCommand.SET, instance, callback);
    }

    @Override
    public CacheInstance getSync(String key)
    {
        CacheInstance instance = new CacheInstance(key);
        return this.invokeSync(CacheCommand.GET, instance);
    }

    @Override
    public RedFuture<CacheInstance> getAsync(String key, CacheCallback callback)
    {
        CacheInstance instance = new CacheInstance(key);
        return this.invokeAsync(CacheCommand.GET, instance, callback);
    }

    @Override
    public void deleteSync(String key)
    {
        CacheInstance instance = new CacheInstance(key);
        this.invokeSync(CacheCommand.DELETE, instance);
    }

    @Override
    public void deleteAsync(String key, CacheCallback callback)
    {
        CacheInstance instance = new CacheInstance(key);
        this.invokeAsync(CacheCommand.DELETE, instance, callback);
    }

    private FutureCacheInstance invokeAsync(CacheCommand command, CacheInstance instance, CacheCallback callback)
    {
        this.check(command, instance);
        CacheMessage message = instance.build(CacheMessage.TRANSIENT, command);
        CacheMessageListener messageListener = null;
        if (callback != null)
        {
            messageListener = new CacheMessageListener(callback);
        }
        RedFuture<Message> future = handlerClient.sendMessage(message, messageListener);
        FutureCacheInstance futureCacheInstance = new FutureCacheInstance(future);
        return futureCacheInstance;
    }

    private void check(CacheCommand command, CacheInstance instance)
    {
        AssertUtil.notNull(instance, "CacheInstance");
        AssertUtil.notEmpty(instance.getKey(), "Key");
    }

    private CacheInstance invokeSync(CacheCommand command, CacheInstance instance)
    {
        FutureCacheInstance futureCacheInstance = this.invokeAsync(command, instance, null);
        try
        {
            return futureCacheInstance.get();
        }
        catch (Exception e)
        {
            if (e instanceof RedClientException)
                throw (RedClientException)e;

            throw new RegistryClientException(e);
        }
    }

}
