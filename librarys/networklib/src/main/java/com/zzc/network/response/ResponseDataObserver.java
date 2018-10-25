package com.zzc.network.response;

import com.zzc.baselib.ui.listener.IRxObserveDisposer;

/**
 * Created by Roye on 2016/12/8.
 */

public abstract class ResponseDataObserver<Data> extends BaseResponseObserver<HttpResponse<Data>, Data> {

    public ResponseDataObserver() {
        this(null);
    }

    /**
     * 非必传
     *
     * @param observeDisposer observeDisposer在fragment or activity实现，建议传此参数
     */
    public ResponseDataObserver(IRxObserveDisposer observeDisposer) {
        super(observeDisposer);
    }

    @Override
    public void onFailed(HttpResponse<Data> result) {
        DefaultResponseCodeHandle.handle(result);
        if (result.getMsg() != null) {
        }
    }
}
