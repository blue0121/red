package com.red.core.util;

import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-18
 */
public class AssertUtil
{
	private AssertUtil()
	{
	}

	public static void notNull(Object object, String name)
	{
		if (name == null)
			name = "";

		if (object == null)
			throw new NullPointerException(name + " is null.");
	}

	public static void positive(long num, String name)
	{
		if (name == null)
			name = "";

		if (num <= 0)
			throw new IllegalArgumentException(name + " is non-positive number");
	}

	public static void notEmpty(String str, String name)
	{
		if (name == null)
			name = "";

		if (str == null || str.isEmpty())
			throw new NullPointerException(name + " is empty.");
	}

	public static void notEmpty(Collection list, String name)
	{
		if (name == null)
			name = "";

		if (list == null || list.isEmpty())
			throw new NullPointerException(name + " is empty.");
	}

}
