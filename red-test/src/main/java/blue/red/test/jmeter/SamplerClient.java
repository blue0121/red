package blue.red.test.jmeter;

import blue.red.client.config.RedConfig;
import blue.red.client.net.NettyConnectionClient;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.threads.JMeterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-19
 */
public abstract class SamplerClient extends AbstractJavaSamplerClient
{
	private static Logger logger = LoggerFactory.getLogger(SamplerClient.class);

	protected int thread;
	protected String address;
	protected String token;
	protected NettyConnectionClient client;

	public SamplerClient()
	{
	}


	@Override
	public void setupTest(JavaSamplerContext context)
	{
		JMeterContext ctx = context.getJMeterContext();
		this.thread = ctx.getThreadNum();

		this.address = context.getParameter("address");
		this.token = context.getParameter("token");

		RedConfig redConfig = new RedConfig();
		redConfig.setToken(token);
		client = new NettyConnectionClient(address, redConfig);
	}

	@Override
	public Arguments getDefaultParameters()
	{
		Arguments params = new Arguments();
		params.addArgument("address", "localhost:7903");
		params.addArgument("token", "token");
		return params;
	}

}
