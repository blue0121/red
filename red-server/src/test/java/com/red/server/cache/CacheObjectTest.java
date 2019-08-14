package com.red.server.cache;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertTrue(object.expire() > 0);
        Thread.sleep(50);
        System.out.println(object.expire());
        Assert.assertTrue(object.expire() <= 0);
    }

}
