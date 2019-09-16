package com.red.client.registry;

import com.red.core.message.RegistryCommand;
import com.red.core.message.RegistryItem;
import com.red.core.message.RegistryMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

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
		RegistryMessage message = RegistryMessage.create(RegistryCommand.SAVE, "com.blue.hello");
		message.addItem(new RegistryItem("localhost:8080"));
		message.addItem(new RegistryItem("127.0.0.1:9000"));
		RegistryInstance instance = RegistryInstance.from(message);
		Assert.assertNotNull(instance);
		Assert.assertEquals(1, instance.getNameSet().size());
		Assert.assertEquals("com.blue.hello", instance.getName());
		Assert.assertEquals(2, instance.getHostSet().size());

		Set<Host> hostSet = new HashSet<>();
		hostSet.add(new Host("localhost", 8080));
		hostSet.add(new Host("127.0.0.1", 9000));
		Assert.assertEquals(hostSet, instance.getHostSet());
	}

	@Test
	public void testBuild1()
	{
		RegistryInstance instance = new RegistryInstance("com.blue.hello");
		Host host = Host.parse("localhost:8080");
		host.setToken("token");
		instance.addHost(host);
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		Assert.assertNotNull(message);
		Assert.assertEquals(RegistryCommand.SAVE, message.getCommand());
		Assert.assertEquals("com.blue.hello", message.getName());
		Assert.assertEquals(1, message.itemSize());
		Assert.assertEquals("localhost:8080", message.getItem().getItem());
		Assert.assertEquals("token", message.getItem().getToken());
	}

	@Test
	public void testBuild2()
	{
		RegistryInstance instance = new RegistryInstance("com.blue.hello");
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		Assert.assertNotNull(message);
		Assert.assertEquals(RegistryCommand.SAVE, message.getCommand());
		Assert.assertEquals("com.blue.hello", message.getName());
		Assert.assertEquals(0, message.itemSize());
	}

}
