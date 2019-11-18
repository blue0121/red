package blue.test.red.server.cache;

import blue.red.server.cache.CacheObject;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-08
 */
public class CacheObjectRemovalListener implements RemovalListener<String, CacheObject>
{
	private static Logger logger = LoggerFactory.getLogger(CacheObjectRemovalListener.class);
	
	public CacheObjectRemovalListener()
	{
	}

	@Override
	public void onRemoval(String key, CacheObject value, RemovalCause cause)
	{
		logger.info("remove, key: {}, cause: {}", key, cause);
	}
}
