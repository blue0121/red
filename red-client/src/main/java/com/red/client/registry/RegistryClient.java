package com.red.client.registry;

import com.red.client.RedFuture;

import java.util.concurrent.TimeUnit;

/**
 * @author Jin Zheng
 * @since 2019-05-12
 */
public interface RegistryClient
{

	RegistryInstance saveSync(RegistryInstance instance);

	RedFuture<RegistryInstance> saveAsync(RegistryInstance instance, RegistryCallback callback);

	void saveAtRate(RegistryInstance instance, long period, TimeUnit unit);

	RegistryInstance deleteSync(RegistryInstance instance);

	RedFuture<RegistryInstance> deleteAsync(RegistryInstance instance, RegistryCallback callback);

	RegistryInstance listSync(RegistryInstance instance);

	RedFuture<RegistryInstance> listAsync(RegistryInstance instance, RegistryCallback callback);

	void bind(RegistryInstance instance);

	void unbind(RegistryInstance instance);

	void watch(RegistryInstance instance, RegistryCallback callback);

	void unwatch(RegistryInstance instance);

}
