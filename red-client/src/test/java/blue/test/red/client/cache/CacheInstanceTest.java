package blue.test.red.client.cache;

import blue.red.client.cache.CacheInstance;
import blue.red.core.message.CacheCommand;
import blue.red.core.message.CacheMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheInstanceTest
{
    public CacheInstanceTest()
    {
    }

    @Test
    public void test()
    {
        String key = "key";
        StringBuilder value = new StringBuilder(1024);
        for (int i = 0; i < 200; i++)
        {
            value.append(key);
        }
        CacheInstance instance = new CacheInstance(key, value.toString());
        CacheMessage message = instance.build(CacheMessage.TRANSIENT, CacheCommand.SET);
        Assertions.assertNotNull(message);
        Assertions.assertEquals(key, message.getKey());
        Assertions.assertTrue(message.isCompress());
        Assertions.assertTrue(message.getValue().length < value.length());
    }

}
