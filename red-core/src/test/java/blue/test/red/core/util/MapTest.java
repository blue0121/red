package blue.test.red.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-18
 */
public class MapTest
{
	private ConcurrentMap<String, List<String>> map;

	public MapTest()
	{
	}

	@BeforeEach
	public void before()
	{
		map = new ConcurrentHashMap<>();
	}

	@Test
	public void test1()
	{
		List<String> list = map.putIfAbsent("blue", new ArrayList<>());
		List<String> newList = map.get("blue");
		Assertions.assertNotNull(newList);
		Assertions.assertTrue(newList.isEmpty());
		Assertions.assertNull(list);

	}

	@Test
	public void test2()
	{
		List<String> list = new ArrayList<>();
		list.add("blue");
		map.put("blue", list);
		list = map.putIfAbsent("blue", new ArrayList<>());
		List<String> newList = map.get("blue");
		Assertions.assertNotNull(newList);
		Assertions.assertEquals(1, newList.size());
		Assertions.assertNotNull(list);
		Assertions.assertEquals(1, list.size());
	}

}
