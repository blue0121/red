package com.red.client.cache;

import com.red.client.ConnectionListener;
import com.red.client.ConnectionListenerTest;
import com.red.client.config.RedConfig;
import com.red.client.net.NettyConnectionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheMain
{
    private static Logger logger = LoggerFactory.getLogger(CacheMain.class);

    public static final RedConfig RED_CONFIG = new RedConfig();
    public static final String ADDRESS = "localhost:7903";

    public static void main(String[] args)
    {
        ConnectionListener listener = new ConnectionListenerTest();
        CacheListenerTest callback = new CacheListenerTest();
        NettyConnectionClient client = new NettyConnectionClient(ADDRESS, RED_CONFIG);
        client.addConnectionListener(listener);
        CacheClient cacheClient = new DefaultCacheClient(client);
        client.start();
        test(cacheClient, callback);

        client.stop();
    }

    private static void test(CacheClient client, CacheListenerTest callback)
    {
        String key = "test";
        String value = "testtest";
        client.setSync(new CacheInstance(key, value));
        String rs = client.getSync(key).getValueObject();
        System.out.println(rs);
        client.deleteSync(key);
        rs = client.getSync(key).getValueObject();
        System.out.println(rs);
    }

}
