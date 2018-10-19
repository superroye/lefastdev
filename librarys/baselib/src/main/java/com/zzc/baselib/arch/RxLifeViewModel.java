package com.zzc.baselib.arch;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;

import com.zzc.baselib.ui.listener.IRxObserveDisposer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Roye
 * @date 2018/8/22
 */
public class RxLifeViewModel extends ViewModel implements LifecycleObserver, IRxObserveDisposer {

    //创建subscription的时候，不小心以某种方式持有了context的引用，容易发生泄漏
    // 所以使用CompositeSubscription来持有所有的Subscriptions在activity销毁时取消订阅
    //其他页面采用addSubscription来注册Subscription
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void addDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

}
