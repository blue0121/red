package blue.test.red.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author Jin Zheng
 * @since 1.0 2019-06-21
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/", plugin = { "pretty", "html:target/cucumber"})
public class CucumberTest
{
	public CucumberTest()
	{
	}

}
