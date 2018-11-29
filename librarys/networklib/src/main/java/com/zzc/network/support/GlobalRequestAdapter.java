package com.zzc.network.support;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author Roye
 * @date 2018/8/6
 */
public interface GlobalRequestAdapter {

    void addHeader(Request.Builder builder);

    void addQueryParams(HttpUrl.Builder httpUrlBuilder);
}
