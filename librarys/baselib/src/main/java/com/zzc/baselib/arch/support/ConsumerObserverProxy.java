package com.zzc.baselib.arch.support;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

/**
 * @author Roye
 * @date 2018/11/14
 */
public class ConsumerObserverProxy<T> implements Observer<T> {

    private int version;
    private Observer delegate;

    public ConsumerObserverProxy(Observer observer) {
        this.delegate = observer;
    }

    @Override
    public void onChanged(@Nullable T data) {
        int newVersion;
        if (data == null) {
            newVersion = 0;
        } else {
            newVersion = data.hashCode();
        }
        if (newVersion != version) {
            version = newVersion;
            delegate.onChanged(data);
        }
    }
}
