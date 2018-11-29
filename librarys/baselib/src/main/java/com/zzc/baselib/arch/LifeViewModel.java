package com.zzc.baselib.arch;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Roye
 * @date 2018/8/22
 */
public class LifeViewModel extends ViewModel implements LifecycleObserver {

    public UIBehaviorViewModel mUIBehaviorViewModel;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(LifecycleOwner lifecycleOwner){
        if(lifecycleOwner instanceof Fragment){
            mUIBehaviorViewModel = ViewModelProviders.of((Fragment)lifecycleOwner).get(UIBehaviorViewModel.class);
        }else if(lifecycleOwner instanceof FragmentActivity){
            mUIBehaviorViewModel = ViewModelProviders.of((FragmentActivity)lifecycleOwner).get(UIBehaviorViewModel.class);
        }
    }
}
