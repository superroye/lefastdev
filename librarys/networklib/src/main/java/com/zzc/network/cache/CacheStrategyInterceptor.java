package com.zzc.network.cache;


import com.zzc.baselib.util.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Roye
 * @date 2018/9/10
 * 缓存拦截器，配合网络拦截器一起使用
 */
public class CacheStrategyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oRequest = chain.request();
        boolean connected = NetworkUtils.isAvailable();
        //如果没有网络，则启用 FORCE_CACHE
        if (!connected) {
            oRequest = oRequest.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response originalResponse = chain.proceed(oRequest);
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .build();
        }

        Response tryCacheResponse = CacheStrategyUtil.doForCacheInterceptor(chain, oRequest);
        if (tryCacheResponse != null) {
            return tryCacheResponse;
        }

        return chain.proceed(oRequest);
    }


}
