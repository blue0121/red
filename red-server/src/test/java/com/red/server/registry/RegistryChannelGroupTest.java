package com.red.server.registry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2019-05-25
 */
public class RegistryChannelGroupTest
{
	Map<String, String> idMap;
	Map<String, String> nameMap;

	public RegistryChannelGroupTest()
	{
	}

	@Before
	public void before()
	{
		idMap = new HashMap<>();
		nameMap = new HashMap<>();
	}

	@Test
	public void testIsBindChannel()
	{
		Assert.assertFalse(isBindChannel("001", "red001"));
		Assert.assertTrue(isBindChannel("001", "red001"));
		Assert.assertFalse(isBindChannel("001", "red002"));
		Assert.assertFalse(isBindChannel("002", "red002"));
		Assert.assertFalse(isBindChannel("010", "red010"));
		Assert.assertFalse(isBindChannel("010", "red011"));
		Assert.assertFalse(isBindChannel("010", "red010"));
		System.out.println(idMap);
		System.out.println(nameMap);
		Assert.assertEquals("red002", idMap.get("002"));
		Assert.assertEquals("red010", idMap.get("010"));
		Assert.assertEquals("002", nameMap.get("red002"));
		Assert.assertEquals("010", nameMap.get("red010"));
	}
	public boolean isBindChannel(String id, String name)
	{
		String oldId = nameMap.get(name);
		String oldName = idMap.get(id);
		boolean isBind = id.equals(oldId) && name.equals(oldName);
		if (!isBind)
		{
			nameMap.remove(oldName);
			idMap.remove(oldId);
			nameMap.put(name, id);
			idMap.put(id, name);
		}
		return isBind;
	}

}
