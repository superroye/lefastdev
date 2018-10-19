package com.zzc.baselib.arch;

import android.arch.core.util.Function;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ActionTransformations {

    @MainThread
    public static <X, Y> ActionLiveData<Y> map(@NonNull ActionLiveData<X> source,
                                               @NonNull final Function<ActionEntity<X>, ActionEntity<Y>> func) {
        final ActionLiveData<Y> result = new ActionLiveData<>();
        result.addSource(source, new Observer<ActionEntity<X>>() {
            @Override
            public void onChanged(@Nullable ActionEntity<X> entity) {
                result.setValue(func.apply(entity));
            }
        });
        return result;
    }

    @MainThread
    public static <X, Y> ActionLiveData<Y> switchMap(@NonNull ActionLiveData<X> trigger,
                                                     @NonNull final Function<ActionEntity<X>, ActionLiveData<Y>> func) {
        final ActionLiveData<Y> result = new ActionLiveData<>();
        result.addSource(trigger, new Observer<ActionEntity<X>>() {

            ActionLiveData<Y> mSource;

            @Override
            public void onChanged(@Nullable ActionEntity<X> entity) {
                ActionLiveData<Y> newLiveData = func.apply(entity);
                if (mSource == newLiveData) {
                    return;
                }
                if (mSource != null) {
                    result.removeSource(mSource);
                }
                mSource = newLiveData;
                if (mSource != null) {
                    result.addSource(mSource, new Observer<ActionEntity<Y>>() {
                        @Override
                        public void onChanged(@Nullable ActionEntity<Y> y) {
                            result.setValue(y);
                        }
                    });
                }
            }
        });
        return result;
    }
}