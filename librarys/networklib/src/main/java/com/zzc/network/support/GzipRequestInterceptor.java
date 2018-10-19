package com.zzc.network.support;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class GzipRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request compressedRequest = originalRequest.newBuilder()
                .header("Accept-Encoding", "gzip")
                .build();

        Response response;
        try {
            response = chain.proceed(compressedRequest);

            if (response.header("Content-Encoding") != null && response.header("Content-Encoding").contains("gzip")) {
                return response.newBuilder().body(new GzipResponseBody(response.body())).build();
            } else {
                return response;
            }
        } catch (Exception e) {
            return chain.proceed(originalRequest);
        }
    }
}
