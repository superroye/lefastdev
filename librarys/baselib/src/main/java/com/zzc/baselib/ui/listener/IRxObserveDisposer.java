package com.zzc.baselib.ui.listener;

import io.reactivex.disposables.Disposable;

/**
 * Created by Roye on 2018/5/18.
 */
public interface IRxObserveDisposer {

    public void addDisposable(Disposable disposable);
}
