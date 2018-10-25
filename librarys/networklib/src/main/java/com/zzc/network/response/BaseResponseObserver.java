package com.zzc.network.response;

import com.zzc.baselib.ui.listener.IProgressDialog;
import com.zzc.baselib.ui.listener.IRxObserveDisposer;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Roye on 2016-12-7.
 */

public abstract class BaseResponseObserver<Result, Data> implements Observer<Result>, SupportResponseLifecycle<Result, Data> {

    private Disposable disposable;
    protected SupportProcedure procedure;
    WeakReference<IRxObserveDisposer> mRxObserveDisposer;

    public BaseResponseObserver(IRxObserveDisposer rxDisposer) {
        this(rxDisposer, new SupportProcedure());
    }

    public BaseResponseObserver(IRxObserveDisposer rxDisposer, SupportProcedure procedure) {
        this.procedure = procedure;
        this.procedure.setResponseLifecycle(this);
        if (rxDisposer != null) {
            this.mRxObserveDisposer = new WeakReference<>(rxDisposer);
        }
    }

    public void setProgressDialog(IProgressDialog progressDialog) {
        procedure.setProgressDialog(progressDialog);
    }

    public void setProgressDialog(IProgressDialog progressDialog, String loadingText) {
        procedure.setProgressDialog(progressDialog, loadingText);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mRxObserveDisposer != null) {
            IRxObserveDisposer rxDisposer = mRxObserveDisposer.get();
            if (rxDisposer != null) {
                rxDisposer.addDisposable(d);
            }
        }
        this.disposable = d;
        onStart();
    }

    @Override
    public void onNext(Result result) {
        if (result == null) {
            onResponse(null);
        } else if (result instanceof IHttpResponse) {
            procedure.handleResponse((IHttpResponse) result);
        } else {
            onResponse((Data) result);
        }
    }

    @Override
    public void onComplete() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        this.onFinish();
    }

    @Override
    public void onError(Throwable e) {
        procedure.handle(e);
        if (disposable != null) {
            disposable.dispose();
        }
        this.onFinish();
    }

    @Override
    public void onFinish() {
        procedure.hideLoading();
    }

    @Override
    public void onStart() {
        procedure.showLoading();
    }

}