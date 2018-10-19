package com.zzc.network.response;

/**
 * Created by Roye on 2016/12/8.
 */

public class HttpResponse<Data> extends BaseResponse implements IHttpResponse<Data> {
    public Data data;

    @Override
    public boolean isOk() {
        return code == 0;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Data getData() {
        return data;
    }


}