package com.zzc.baselib.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;

import com.zzc.baselib.arch.ActionEntity;
import com.zzc.baselib.arch.UIBehaviorViewModel;

/**
 * @author Roye
 * @date 2018/11/14
 */
public class BaseLiveActivity extends BaseActivity {

    public void addBaseUIObserver() {
        UIBehaviorViewModel viewModel = ViewModelProviders.of(this).get(UIBehaviorViewModel.class);
        viewModel.loading().observe(this, new Observer<ActionEntity>() {
            @Override
            public void onChanged(@Nullable ActionEntity data) {
                if (data != null) {
                    if (data.id == UIBehaviorViewModel.LOADING_SHOW) {
                        if (data.extra != null && data.extra[0] != null) {
                            showLoading(String.valueOf(data.extra[0]));
                        }
                    } else {
                        hideLoading();
                    }
                }
            }
        });
        viewModel.animation().observe(this, new Observer<ActionEntity>() {
            @Override
            public void onChanged(@Nullable ActionEntity data) {
                if (data != null) {
                }
            }
        });
    }
}
