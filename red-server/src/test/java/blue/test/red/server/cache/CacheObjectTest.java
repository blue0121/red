package blue.test.red.server.cache;

import blue.red.server.cache.CacheObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheObjectTest
{

    public CacheObjectTest()
    {
    }

    @Test
    public void testExpire() throws Exception
    {
        CacheObject object = new CacheObject();
        object.setTtl(20);
        System.out.println(object.expire());
        Assertions.assertTrue(object.expire() > 0);
        Thread.sleep(50);
        System.out.println(object.expire());
        Assertions.assertTrue(object.expire() <= 0);
    }

}
