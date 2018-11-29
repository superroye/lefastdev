package com.zzc.baselib.arch;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zzc.baselib.arch.support.ConsumerObserverProxy;

import java.util.LinkedHashMap;

/**
 * @author Roye
 * @date 2018/11/14
 * 1、跟普通的livedata不同，它是每次setvalue只通知一次，不会因为UI active而再度通知
 * 2、适合用于弹toast，一次性动画场景
 */
public class ConsumerLiveData<T> extends MediatorLiveData<T> {

    private MediatorLiveData<T> delegate;

    public ConsumerLiveData() {
        delegate = new MediatorLiveData();
    }

    @Override
    public void postValue(T value) {
        delegate.postValue(value);
    }

    @Override
    public void setValue(T value) {
        delegate.setValue(value);
    }

    @Override
    public <S> void addSource(@NonNull LiveData<S> source, @NonNull Observer<S> onChanged) {
        delegate.addSource(source, onChanged);
    }

    @Nullable
    @Override
    public T getValue() {
        return delegate.getValue();
    }

    @Override
    public boolean hasActiveObservers() {
        return delegate.hasActiveObservers();
    }

    @Override
    public boolean hasObservers() {
        return delegate.hasObservers();
    }

    private LinkedHashMap<Observer<T>, ConsumerObserverProxy> mObservers =
            new LinkedHashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        ConsumerObserverProxy<T> proxy = mObservers.get(observer);

        if (proxy == null) {
            proxy = new ConsumerObserverProxy(observer);
            mObservers.put(observer, proxy);
        }

        delegate.observe(owner, proxy);
    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        ConsumerObserverProxy proxy = new ConsumerObserverProxy(observer);
        mObservers.put(observer, proxy);

        delegate.observeForever(proxy);
    }

    @Override
    public void removeObserver(@NonNull Observer<T> observer) {
        ConsumerObserverProxy proxy = mObservers.get(observer);
        if (proxy != null) {
            delegate.removeObserver(proxy);
        }
    }

    @Override
    public void removeObservers(@NonNull LifecycleOwner owner) {
        delegate.removeObservers(owner);
    }

    @Override
    public <S> void removeSource(@NonNull LiveData<S> toRemote) {
        delegate.removeSource(toRemote);
    }
}
