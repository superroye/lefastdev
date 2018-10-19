package com.zzc.network.support;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

public final class GzipResponseBody extends ResponseBody {
    private final ResponseBody responseBody;

    public GzipResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        GzipSource source = new GzipSource(responseBody.source());
        try {
            return Okio.buffer(source);
        } finally {
        }
    }

}