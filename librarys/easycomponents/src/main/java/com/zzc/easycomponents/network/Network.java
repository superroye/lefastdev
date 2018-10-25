package com.zzc.easycomponents.network;

import com.zzc.network.ApiBuilderOuter;
import com.zzc.network.support.GlobalRequestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roye
 * @date 2018/10/19
 */
public class Network {

    private Network() {
    }

    public static Network getInstance() {
        if (mInstance == null) {
            mInstance = new Network();
            mInstance.apis = new ArrayList();
        }
        return mInstance;
    }

    private static Network mInstance;
    private List apis;

    public <T> T api(Class<T> apiClass) {
        for (int i = 0; i < apis.size(); i++) {
            if (apiClass.isAssignableFrom(apis.get(i).getClass())) {
                return (T) apis.get(i);
            }
        }
        return null;
    }

    public <T> ApiBuilderFacade<T> builder(final Class<T> apiClass) {
        ApiBuilderFacade<T> facade = new ApiBuilderFacade<T>() {
            ApiBuilderOuter<T> apiBuilder = new ApiBuilderOuter();

            @Override
            public ApiBuilderFacade baseUrl(String baseUrl) {
                apiBuilder.baseUrl(baseUrl);
                return this;
            }

            @Override
            public ApiBuilderFacade setGlobalRequestAdapter(GlobalRequestAdapter globalRequestAdapter) {
                apiBuilder.setGlobalRequestAdapter(globalRequestAdapter);
                return this;
            }

            @Override
            public ApiBuilderOuter getApiBuilder() {
                return apiBuilder;
            }

            @Override
            public T build() {
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
                    mInstance.apis.add(api);
                }
                return api;
            }
        };

        return facade;
    }
}
