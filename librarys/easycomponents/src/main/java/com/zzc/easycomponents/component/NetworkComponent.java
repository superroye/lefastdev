package com.zzc.easycomponents.component;

import com.zzc.easycomponents.base.IBuilder;
import com.zzc.easycomponents.base.IComponent;

import java.util.concurrent.TimeUnit;

/**
 * @author Roye
 * @date 2018/11/12
 */
public interface NetworkComponent extends IComponent {

    public <API> INetworkBuilder builder(Class<API> apiClass);

    public <API> API api(Class<API> apiClass);

    public interface INetworkBuilder extends IBuilder {

        @Override
        public NetworkComponent build();

        public INetworkBuilder baseUrl(String baseUrl);

        public INetworkBuilder requestAdapter(Object globalRequestAdapter);

        public INetworkBuilder connectTimeout(long timeout, TimeUnit unit);

        public INetworkBuilder readTimeout(long timeout, TimeUnit unit);

        public INetworkBuilder cookieJar(Object cookieJar);

        public INetworkBuilder cache(Object cache);
    }
}
