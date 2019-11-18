package blue.test.red.client.cache;

import blue.red.client.ConnectionListener;
import blue.red.client.cache.CacheClient;
import blue.red.client.cache.CacheInstance;
import blue.red.client.cache.DefaultCacheClient;
import blue.red.client.config.RedConfig;
import blue.red.client.net.NettyConnectionClient;
import blue.test.red.client.ConnectionListenerTest;
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
        StringBuilder value = new StringBuilder(1024);
        for (int i = 0; i < 200; i++)
        {
            value.append(key);
        }
        client.setSync(new CacheInstance(key, value.toString()));
        String rs = client.getSync(key).getValueObject();
        System.out.println(rs);
        client.deleteSync(key);
        rs = client.getSync(key).getValueObject();
        System.out.println(rs);
    }

}
