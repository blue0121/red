package com.red.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.io.IOException;

public class CompressUtil
{
    private static Logger logger = LoggerFactory.getLogger(CompressUtil.class);

    private CompressUtil()
    {
    }

    public static byte[] compress(byte[] in)
    {
        if (in == null || in.length == 0)
            return in;

        byte[] out = null;
        try
        {
            out = Snappy.compress(in);
        }
        catch (IOException e)
        {
            logger.warn("compress error, ", e);
        }
        return out;
    }

    public static byte[] uncompress(byte[] in)
    {
        if (in == null || in.length == 0)
            return in;

        byte[] out = null;
        try
        {
            out = Snappy.uncompress(in);
        }
        catch (IOException e)
        {
            logger.warn("uncompress error, ", e);
        }
        return out;
    }

}
