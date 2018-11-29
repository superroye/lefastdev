package com.zzc.baselib.easycomponent;

import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import com.zzc.baselib.util.ToastUtils;
import com.zzc.easycomponents.util.Toast;

/**
 * @author Roye
 * @date 2018/11/13
 */
@EasyUtil
public class ToastImpl extends Toast {
    @Override
    public boolean isTooFast() {
        return ToastUtils.isTooFast();
    }

    @Override
    public boolean isTooFast(int delay) {
        return ToastUtils.isTooFast(delay);
    }

    @Override
    public boolean isSame(String msg) {
        return ToastUtils.isSame(msg);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void showToast(int resId) {
        ToastUtils.showToast(resId);
    }

    @Override
    public void showToastAtCenter(String msg) {
        ToastUtils.showToastAtCenter(msg);
    }

    @Override
    public void showToastAtCenter(int resId) {
        ToastUtils.showToastAtCenter(resId);
    }

    @Override
    public void showToastAtTop(String msg) {
        ToastUtils.showToastAtTop(msg);
    }

    @Override
    public void showToastAtTop(int resId) {
        ToastUtils.showToastAtTop(resId);
    }
}
