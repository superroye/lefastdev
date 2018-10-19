package com.zzc.baselib.support.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Roye
 * @date 2018/9/6
 */
public class RxSimple {

    public static <T> Observable<T> createSubscriber(ObservableOnSubscribe<T> observableOnSubscribe) {
        Observable<T> observable = Observable.create(observableOnSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public static <T> Observable<T> createMap(Function<T, T> function, T input) {
        Observable<T> observable = Observable.just(input).map(function).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
