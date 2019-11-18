package blue.test.red.server.cache;

import blue.red.server.cache.CacheObject;
import blue.red.server.cache.CacheObjectExpiry;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-08
 */
public class CaffeineCacheTest
{
	private CacheObjectExpiry expiry;
	private CacheObjectRemovalListener removalListener;
	private Cache<String, CacheObject> cache;

	public CaffeineCacheTest()
	{
		this.expiry = new CacheObjectExpiry();
		this.removalListener = new CacheObjectRemovalListener();
	}

	@Before
	public void before()
	{
		this.cache = Caffeine.newBuilder()
				.expireAfter(expiry)
				.maximumSize(10_000)
				.removalListener(removalListener)
				.build();
	}

	@Test
	public void testCreate()
	{
		String key = "blue";
		long ttl = 100;
		CacheObject object = new CacheObject(key.getBytes(), ttl);
		cache.put(key, object);
		CacheObject object2 = cache.getIfPresent(key);
		Assert.assertNotNull(object2);
		Assert.assertArrayEquals(object.getValue(), object2.getValue());

		this.sleep(150);

		CacheObject object3 = cache.getIfPresent(key);
		Assert.assertNull(object3);
	}

	@Test
	public void testUpdate()
	{
		String key = "blue";
		long ttl = 100;
		CacheObject object = new CacheObject(key.getBytes(), ttl);
		cache.put(key, object);
		CacheObject object2 = cache.getIfPresent(key);
		Assert.assertNotNull(object2);
		Assert.assertArrayEquals(object.getValue(), object2.getValue());

		this.sleep(50);

		CacheObject object3 = cache.getIfPresent(key);
		Assert.assertNotNull(object3);

		CacheObject object4 = new CacheObject(key.getBytes(), ttl);
		cache.put(key, object4);

		this.sleep(50);

		CacheObject object5 = cache.getIfPresent(key);
		Assert.assertNotNull(object5);
		cache.put(key, object4);

		this.sleep(700);

		CacheObject object6 = cache.getIfPresent(key);
		Assert.assertNull(object6);

	}

	private void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
