package com.zzc.baselib.arch;

import android.support.annotation.Nullable;

/**
 * @author Roye
 * @date 2018/9/29
 */
public abstract class ActionOnceObserver<T> implements ActionObserver<T> {

    private int version;

    @Override
    public void onChanged(@Nullable ActionEntity<T> data) {
        int newVersion;
        if (data == null) {
            newVersion = 0;
        } else {
            newVersion = data.hashCode();
        }
        if (newVersion != version) {
            version = newVersion;
            onChangeOnce(data);
        } else {
            version = newVersion;
        }
    }

    public abstract void onChangeOnce(@Nullable ActionEntity<T> data);
}
