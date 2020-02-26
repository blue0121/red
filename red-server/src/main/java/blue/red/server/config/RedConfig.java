package blue.red.server.config;

import blue.red.core.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Jin Zheng
 * @since 2019-06-07
 */
public class RedConfig
{
	private static Logger logger = LoggerFactory.getLogger(RedConfig.class);
	public static final String CONFIG = "red.properties";

	private Properties config;

	private static RedConfig instance;

	private RedConfig()
	{
		this.config = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = loader.getResourceAsStream(CONFIG))
		{
			this.config.load(is);
			this.updateEnv();
			logger.info("Read config: {}{}", CONFIG, this);
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("Not found config: " + CONFIG);
		}
	}

	private void updateEnv()
	{
		Map<String, String> env = System.getenv();
		for (Map.Entry<Object, Object> entry : config.entrySet())
		{
			String key = entry.getKey().toString().toLowerCase();
			if (!env.containsKey(key))
			{
				key = key.toUpperCase();
				if (!env.containsKey(key))
					continue;
			}

			String val = env.get(key);
			config.setProperty(entry.getKey().toString(), val);
		}
	}

	public static RedConfig getInstance()
	{
		if (instance == null)
		{
			synchronized (RedConfig.class)
			{
				if (instance == null)
				{
					instance = new RedConfig();
				}
			}
		}
		return instance;
	}

	public String getString(String key, String defaultValue)
	{
		AssertUtil.notEmpty(key, "Key");
		AssertUtil.notEmpty(defaultValue, "DefaultValue");
		String value = config.getProperty(key, defaultValue);
		return value;
	}

	public int getInt(String key, int defaultValue)
	{
		AssertUtil.notEmpty(key, "Key");
		String value = config.getProperty(key);
		if (value == null || value.isEmpty()) return defaultValue;

		try
		{
			defaultValue = Integer.parseInt(value);
		}
		catch (NumberFormatException e)
		{
			logger.error("Error: ", e);
		}
		return defaultValue;
	}

	public long getLong(String key, long defaultValue)
	{
		AssertUtil.notEmpty(key, "Key");
		String value = config.getProperty(key);
		if (value == null || value.isEmpty()) return defaultValue;

		try
		{
			defaultValue = Long.parseLong(value);
		}
		catch (NumberFormatException e)
		{
			logger.error("Error: ", e);
		}
		return defaultValue;
	}

	public double getDouble(String key, double defaultValue)
	{
		AssertUtil.notEmpty(key, "Key");
		String value = config.getProperty(key);
		if (value == null || value.isEmpty()) return defaultValue;

		try
		{
			defaultValue = Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			logger.error("Error: ", e);
		}
		return defaultValue;
	}

	public List<String> getStringList(String key)
	{
		AssertUtil.notEmpty(key, "Key");
		List<String> stringList = new ArrayList<>();
		String value = config.getProperty(key);
		if (value == null || value.isEmpty()) return stringList;

		String[] values = value.split("[\\s;,\\|]");
		for (String val : values)
		{
			stringList.add(val);
		}
		return stringList;
	}

	public List<Integer> getIntList(String key)
	{
		return this.getStringList(key).stream().map(Integer::valueOf).collect(Collectors.toList());
	}

	public List<Long> getLongList(String key)
	{
		return this.getStringList(key).stream().map(Long::valueOf).collect(Collectors.toList());
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder(1024);
		for (Map.Entry<Object, Object> entry : config.entrySet())
		{
			str.append("\n").append(entry.getKey()).append(" = ").append(entry.getValue());
		}
		str.append("\n");
		return str.toString();
	}
}
