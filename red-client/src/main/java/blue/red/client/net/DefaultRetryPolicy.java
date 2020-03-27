package blue.red.client.net;

import java.util.Random;

/**
 * Unlimited retry
 *
 * @author Jin Zheng
 * @since 1.0 2020-03-27
 */
public class DefaultRetryPolicy implements RetryPolicy
{
	private final Random random = new Random();
	private final int sleepTimeMs;
	private final int num = 100;

	public DefaultRetryPolicy(int sleepTimeMs)
	{
		this.sleepTimeMs = sleepTimeMs;
	}

	@Override
	public boolean allowRetry(int retryCount)
	{
		return true;
	}

	@Override
	public int getSleepTimeMs(int retryCount)
	{
		int sleep = sleepTimeMs + random.nextInt(num);
		return sleep;
	}
}
