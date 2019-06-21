package com.red.test.cucumber;

import cucumber.api.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-21
 */
public class RegistrySteps
{
	private static Logger logger = LoggerFactory.getLogger(RegistrySteps.class);

	private RegistryClientFactory clientFactory = RegistryClientFactory.getInstance();

	public RegistrySteps()
	{
	}

	@Given("start registry client {string}")
	public void start(String name)
	{
		clientFactory.start(name);
	}

	@Given("stop add registry client")
	public void stop()
	{
		clientFactory.stopAll();
	}

}
