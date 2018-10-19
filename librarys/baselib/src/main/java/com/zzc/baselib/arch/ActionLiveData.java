package com.zzc.baselib.arch;

import android.arch.lifecycle.MediatorLiveData;

/**
 *
 * @param <T>
 */
public class ActionLiveData<T> extends MediatorLiveData<ActionEntity<T>> {

    public void setJustValue(T data) {
        super.setValue(new ActionEntity<>(data));
    }

    public void postJustValue(T data) {
        super.postValue(new ActionEntity<>(data));
    }
}