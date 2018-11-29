package com.zzc.network.support;

/**
 * @author Roye
 * @date 2018/10/31
 */
public class RequestKey {

    private String key;

    public RequestKey() {
        reset();
    }

    public void reset() {
        key = String.valueOf(System.currentTimeMillis());
    }

    public String requestKey() {
        return key;
    }
}
