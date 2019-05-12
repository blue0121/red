package com.red.client.registry;

import java.util.concurrent.Future;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public interface RegistryClient
{

	RegistryInstance saveSync(RegistryInstance instance);

	Future<RegistryInstance> saveAsync(RegistryInstance instance, RegistryCallback callback);

	RegistryInstance deleteSync(RegistryInstance instance);

	Future<RegistryInstance> deleteAsync(RegistryInstance instance, RegistryCallback callback);

	RegistryInstance listSync(RegistryInstance instance);

	Future<RegistryInstance> listAsync(RegistryInstance instance, RegistryCallback callback);

	void watch(RegistryInstance instance, RegistryCallback callback);

	void unwatch(RegistryInstance instance);

}
