package com.zzc.baselib.ui.viewtools;

import android.view.View;

/**
 * @author Roye
 * @date 2018/10/8
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (RepeatClickChecker.invalidClick(v)) {
            return;
        }
        onSingleClick(v);
    }

    public abstract void onSingleClick(View view);
}
