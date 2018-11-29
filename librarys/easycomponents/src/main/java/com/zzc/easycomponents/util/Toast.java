package com.zzc.easycomponents.util;

import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

/**
 * @author Roye
 * @date 2018/11/13
 */
public abstract class Toast implements IUtil {

    public abstract boolean isTooFast();

    public abstract boolean isTooFast(int delay);

    public abstract boolean isSame(String msg);

    public abstract void showToast(final String msg);

    public abstract void showToast(int resId);

    public abstract void showToastAtCenter(String msg);

    public abstract void showToastAtCenter(int resId);

    public abstract void showToastAtTop(String msg);

    public abstract void showToastAtTop(int resId);

    @Override
    public String name() {
        return UtilFunction.toast.name();
    }
}
