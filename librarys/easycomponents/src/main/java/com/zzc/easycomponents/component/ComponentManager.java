package com.zzc.easycomponents.component;

import com.zzc.easycomponents.component.proxy.NetworkComponentProxy;
import com.zzc.easycomponents.support.ComponentRuntimeException;

/**
 * @author Roye
 * @date 2018/11/12
 */
public class ComponentManager {

    private NetworkComponent mNetworkComponent;

    public void init(NetworkComponent component) {
        if (this.mNetworkComponent == null) {
            this.mNetworkComponent = new NetworkComponentProxy(component);
        }
    }

    protected NetworkComponent networkComponent() {
        if (mNetworkComponent == null) {
            throw new ComponentRuntimeException("network component has not initialized");
        }
        return mNetworkComponent;
    }
}
