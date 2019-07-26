package com.red.client.registry;

/**
 * @author Jin Zheng
 * @since 1.0 2019-07-26
 */
public interface RegistryCallback
{

	void onSuccess(RegistryInstance data);

	void onFailure(Exception e);

}
