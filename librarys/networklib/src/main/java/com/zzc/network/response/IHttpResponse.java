package com.zzc.network.response;

/**
 * @author Roye
 * @date 2018/9/26
 */
public interface IHttpResponse<Data> {

    public boolean isOk();
    public String getMsg();
    public Data getData();
}
