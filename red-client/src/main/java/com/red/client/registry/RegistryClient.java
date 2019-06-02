package com.red.client.registry;

import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public interface RegistryClient
{

	RegistryInstance saveSync(RegistryInstance instance);

	Future<RegistryInstance> saveAsync(RegistryInstance instance, RegistryListener listener);

	RegistryInstance deleteSync(RegistryInstance instance);

	Future<RegistryInstance> deleteAsync(RegistryInstance instance, RegistryListener listener);

	RegistryInstance listSync(RegistryInstance instance);

	Future<RegistryInstance> listAsync(RegistryInstance instance, RegistryListener listener);

	void watch(RegistryInstance instance, RegistryListener listener);

	void unwatch(RegistryInstance instance);

}
