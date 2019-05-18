package com.red.client.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryMessage;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jin Zheng
 * @since 1.0 2019-05-14
 */
public class RegistryInstanceTest
{
	public RegistryInstanceTest()
	{
	}

	@Test
	public void testFrom()
	{
		RegistryMessage message = RegistryMessage.create(RegistryCommand.SAVE, "blue:com.blue.hello");
		message.addItem("localhost:8080");
		message.addItem("127.0.0.1:9000");
		RegistryInstance instance = RegistryInstance.from(message);
		Assert.assertNotNull(instance);
		Assert.assertEquals("blue", instance.getPrefix());
		Assert.assertEquals("com.blue.hello", instance.getName());
		Assert.assertEquals(2, instance.getHostList().size());

		Host host0 = instance.getHostList().get(0);
		Assert.assertEquals("localhost", host0.getIp());
		Assert.assertEquals(8080, host0.getPort());
		Host host1 = instance.getHostList().get(1);
		Assert.assertEquals("127.0.0.1", host1.getIp());
		Assert.assertEquals(9000, host1.getPort());
	}

	@Test
	public void testBuild1()
	{
		RegistryInstance instance = new RegistryInstance("com.blue.hello");
		instance.addHost("localhost", 8080);
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		Assert.assertNotNull(message);
		Assert.assertEquals(RegistryCommand.SAVE, message.getCommand());
		Assert.assertEquals("com.blue.hello", message.getName());
		Assert.assertEquals(1, message.itemSize());
		Assert.assertEquals("localhost:8080", message.getItem());
	}

	@Test
	public void testBuild2()
	{
		RegistryInstance instance = new RegistryInstance("blue", "com.blue.hello");
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		Assert.assertNotNull(message);
		Assert.assertEquals(RegistryCommand.SAVE, message.getCommand());
		Assert.assertEquals("blue:com.blue.hello", message.getName());
		Assert.assertEquals(0, message.itemSize());
	}

}
