package blue.test.red.client.registry;

import blue.red.client.registry.Host;
import blue.red.client.registry.RegistryInstance;
import blue.red.core.message.RegistryCommand;
import blue.red.core.message.RegistryItem;
import blue.red.core.message.RegistryMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(1, instance.getNameSet().size());
		Assertions.assertEquals("com.blue.hello", instance.getName());
		Assertions.assertEquals(2, instance.getHostSet().size());

		Set<Host> hostSet = new HashSet<>();
		hostSet.add(new Host("localhost", 8080));
		hostSet.add(new Host("127.0.0.1", 9000));
		Assertions.assertEquals(hostSet, instance.getHostSet());
	}

	@Test
	public void testBuild1()
	{
		RegistryInstance instance = new RegistryInstance("com.blue.hello");
		Host host = Host.parse("localhost:8080");
		host.setToken("token");
		instance.addHost(host);
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		Assertions.assertNotNull(message);
		Assertions.assertEquals(RegistryCommand.SAVE, message.getCommand());
		Assertions.assertEquals("com.blue.hello", message.getName());
		Assertions.assertEquals(1, message.itemSize());
		Assertions.assertEquals("localhost:8080", message.getItem().getItem());
		Assertions.assertEquals("token", message.getItem().getToken());
	}

	@Test
	public void testBuild2()
	{
		RegistryInstance instance = new RegistryInstance("com.blue.hello");
		RegistryMessage message = instance.build(RegistryCommand.SAVE);
		Assertions.assertNotNull(message);
		Assertions.assertEquals(RegistryCommand.SAVE, message.getCommand());
		Assertions.assertEquals("com.blue.hello", message.getName());
		Assertions.assertEquals(0, message.itemSize());
	}

}
