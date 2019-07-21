package com.red.server.queue;

/**
 * @author Jin Zheng
 * @since 2019-07-20
 */
public interface HashKey
{

	int calculate(String key, int length);

}
