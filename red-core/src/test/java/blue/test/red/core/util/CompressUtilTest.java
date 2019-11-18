package blue.test.red.core.util;

import blue.red.core.util.CompressUtil;
import org.junit.Assert;
import org.junit.Test;

public class CompressUtilTest
{

    public CompressUtilTest()
    {
    }

    @Test
    public void test()
    {
        String test = "Hello snappy-java! Snappy-java is a JNI-based wrapper of Snappy, a fast compresser/decompresser.";

        byte[] src = test.getBytes();
        System.out.println("src size: " + src.length);
        byte[] dest = CompressUtil.compress(src);
        System.out.println("dest size: " + dest.length);
        String str = new String(CompressUtil.uncompress(dest));
        Assert.assertEquals(test, str);
    }

}
