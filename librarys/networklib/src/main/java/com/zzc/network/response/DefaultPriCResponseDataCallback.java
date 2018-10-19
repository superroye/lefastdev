package com.zzc.network.response;

/**
 * @author Roye
 * @date 2018/9/28
 */
public abstract class DefaultPriCResponseDataCallback<Data> extends PriorityCacheResponseDataCallback<HttpResponse<Data>, Data> {

    @Override
    public void onFailed(HttpResponse<Data> result) {
        super.onFailed(result);
    }
}
