package com.red.test.cucumber;

import com.red.client.cache.CacheClient;
import com.red.client.cache.CacheInstance;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheSteps
{
    private static Logger logger = LoggerFactory.getLogger(CacheSteps.class);

    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String TTL = "ttl";
    public static final String NULL = "(null)";

    private ClientFactory clientFactory = ClientFactory.getInstance();

    public CacheSteps()
    {
    }

    @Given("start cache client {string}")
    public void start(String name)
    {
        clientFactory.start(name);
    }

    @Given("stop cache client {string}")
    public void stop(String name)
    {
        clientFactory.stop(name);
    }

    @When("cache client {string} {string}:")
    public void handle(String name, String opt, List<Map<String, String>> dataTable)
    {
        Assert.assertTrue("No Data", dataTable != null && !dataTable.isEmpty());
        CacheClient client = clientFactory.getCacheClient(name);
        Assert.assertNotNull("No Cache Client", client);

        Map<String, CacheInstance> map = this.getCacheInstance(dataTable);
        if ("set".equals(opt))
        {
            for (Map.Entry<String, CacheInstance> entry : map.entrySet())
            {
                client.setSync(entry.getValue());
            }
        }
        else if ("delete".equals(opt))
        {
            for (Map.Entry<String, CacheInstance> entry : map.entrySet())
            {
                client.deleteSync(entry.getKey());
            }
        }
        else if ("get".equals(opt))
        {
            for (Map.Entry<String, CacheInstance> entry : map.entrySet())
            {
                CacheInstance dest = client.getSync(entry.getKey());
                Assert.assertArrayEquals(entry.getValue().getValue(), dest.getValue());
            }
        }
    }


    private Map<String, CacheInstance> getCacheInstance(List<Map<String, String>> dataTable)
    {
        Map<String, CacheInstance> map = new HashMap<>();
        if (dataTable == null || dataTable.isEmpty())
            return map;

        for (Map<String, String> data : dataTable)
        {
            String key = data.get(KEY);
            if (key == null || key.isEmpty())
                continue;

            CacheInstance instance = map.computeIfAbsent(key, k -> new CacheInstance(k));
            String value = data.get(VALUE);
            if (value != null && !value.isEmpty() && !value.equals(NULL))
            {
                instance.setValueObject(value);
            }
            String ttl = data.get(TTL);
            if (ttl != null && !ttl.isEmpty())
            {
                instance.setTtl(Long.parseLong(ttl));
            }
        }
        return map;
    }

}
