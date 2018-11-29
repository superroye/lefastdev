package com.zzc.network.easycomponent;

import com.zzc.easycomponents.component.NetworkComponent;
import com.zzc.easycomponents.support.ComponentRuntimeException;
import com.zzc.network.ApiBuilderOuter;
import com.zzc.network.support.GlobalRequestAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CookieJar;

/**
 * @author Roye
 * @date 2018/11/13
 */
public class NetworkComponentImpl implements NetworkComponent {

    private List apis;

    public NetworkComponentImpl() {
        apis = new ArrayList();
    }

    @Override
    public <T> INetworkBuilder builder(final Class<T> apiClass) {
        INetworkBuilder builder = new INetworkBuilder() {
            ApiBuilderOuter<T> apiBuilder = new ApiBuilderOuter();

            @Override
            public NetworkComponent build() {
                T api = null;
                for (int i = 0; i < apis.size(); i++) {
                    if (apiClass == apis.get(i).getClass()) {
                        api = (T) apis.get(i);
                        break;
                    }
                }
                if (api == null) {
                    apiBuilder.forType(apiClass);
                    api = apiBuilder.build();
                    NetworkComponentImpl.this.apis.add(api);
                }

                return NetworkComponentImpl.this;
            }

            @Override
            public INetworkBuilder baseUrl(String baseUrl) {
                apiBuilder.baseUrl(baseUrl);
                return this;
            }

            @Override
            public INetworkBuilder requestAdapter(Object globalRequestAdapter) {
                if (globalRequestAdapter instanceof GlobalRequestAdapter) {
                    apiBuilder.setGlobalRequestAdapter((GlobalRequestAdapter) globalRequestAdapter);
                }
                return this;
            }

            @Override
            public INetworkBuilder connectTimeout(long timeout, TimeUnit unit) {
                apiBuilder.builder().connectTimeout(timeout, unit);
                return this;
            }

            @Override
            public INetworkBuilder readTimeout(long timeout, TimeUnit unit) {
                apiBuilder.builder().readTimeout(timeout, unit);
                return this;
            }

            @Override
            public INetworkBuilder cookieJar(Object cookieJar) {
                if (cookieJar instanceof CookieJar) {
                    apiBuilder.builder().cookieJar((CookieJar) cookieJar);
                }
                return this;
            }

            @Override
            public INetworkBuilder cache(Object cache) {
                if (cache instanceof Cache) {
                    apiBuilder.builder().cache((Cache) cache);
                }
                return this;
            }
        };
        return builder;
    }

    @Override
    public <T> T api(Class<T> apiClass) {
        for (int i = 0; i < apis.size(); i++) {
            if (apiClass.isAssignableFrom(apis.get(i).getClass())) {
                return (T) apis.get(i);
            }
        }
        throw new ComponentRuntimeException("this api is not inited");
    }


    @Override
    public void start() {
        //不做处理
    }

    @Override
    public void destroy() {
        //不做处理
    }
}
