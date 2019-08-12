package com.red.client;

import java.util.concurrent.Future;

public interface RedFuture<T> extends Future<T>
{

    RedClientException getException();

}
