package com.zzc.easycomponents.util;

import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

/**
 * @author Roye
 * @date 2018/11/13
 */
public abstract class Dimen implements IUtil {

    public abstract int dp2px(float dipValue);

    public abstract int px2dp(float pxValue);

    public abstract int getScreenMinEdge();

    public abstract int getScreenHeight();

    public abstract int getScreenWidth();

    public abstract int getStatusBarHeight();

    @Override
    public String name() {
        return UtilFunction.dimen.name();
    }
}
