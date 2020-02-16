package blue.test.red.server.registry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
	public void before()
	{
		idMap = new HashMap<>();
		nameMap = new HashMap<>();
	}

	@Test
	public void testIsBindChannel()
	{
		Assertions.assertFalse(isBindChannel("001", "red001"));
		Assertions.assertTrue(isBindChannel("001", "red001"));
		Assertions.assertFalse(isBindChannel("001", "red002"));
		Assertions.assertFalse(isBindChannel("002", "red002"));
		Assertions.assertFalse(isBindChannel("010", "red010"));
		Assertions.assertFalse(isBindChannel("010", "red011"));
		Assertions.assertFalse(isBindChannel("010", "red010"));
		System.out.println(idMap);
		System.out.println(nameMap);
		Assertions.assertEquals("red002", idMap.get("002"));
		Assertions.assertEquals("red010", idMap.get("010"));
		Assertions.assertEquals("002", nameMap.get("red002"));
		Assertions.assertEquals("010", nameMap.get("red010"));
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
