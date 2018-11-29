package com.zzc.baselib.arch;

import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.zzc.baselib.ui.listener.IProgressDialog;

/**
 * @author Roye
 * @date 2018/11/14
 */
public final class UIBehaviorViewModel extends ViewModel implements IProgressDialog {

    public static final int LOADING_HIDE = 0;
    public static final int LOADING_SHOW = 1;
    private ConsumerLiveData<ActionEntity> loading;
    private ConsumerLiveData<ActionEntity> animation;

    public UIBehaviorViewModel() {
        loading = new ConsumerLiveData();
        animation = new ConsumerLiveData();
    }

    public ConsumerLiveData<ActionEntity> loading() {
        return loading;
    }

    public ConsumerLiveData<ActionEntity> animation() {
        return animation;
    }

    @Override
    public void showLoading(String text) {
        String text1 = text;
        if(TextUtils.isEmpty(text1)){
            text = "loading...";
        }
        loading().postValue(new ActionEntity(LOADING_SHOW, new Object[]{text}));
    }

    @Override
    public void hideLoading() {
        loading().postValue(new ActionEntity(LOADING_HIDE, null));
    }
}
