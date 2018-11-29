package com.zzc.easycomponents.component.proxy;

import com.zzc.easycomponents.component.NetworkComponent;
import com.zzc.easycomponents.support.ComponentRuntimeException;

/**
 * @author Roye
 * @date 2018/11/13
 */
public class NetworkComponentProxy implements NetworkComponent {

    NetworkComponent delegate;

    public NetworkComponentProxy(NetworkComponent real) {
        this.delegate = real;
    }

    @Override
    public <T> INetworkBuilder builder(Class<T> apiClass) {
        return delegate.builder(apiClass);
    }

    @Override
    public void start() {
        throw new ComponentRuntimeException("cannot be call");
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

    @Override
    public <T> T api(Class<T> apiClass) {
        return delegate.api(apiClass);
    }
}
