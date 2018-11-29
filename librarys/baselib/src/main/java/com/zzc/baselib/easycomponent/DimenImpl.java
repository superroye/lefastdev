package com.zzc.baselib.easycomponent;

import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import com.zzc.baselib.base.AppBase;
import com.zzc.baselib.util.UIUtils;
import com.zzc.easycomponents.util.Dimen;

/**
 * @author Roye
 * @date 2018/11/13
 */
@EasyUtil
public class DimenImpl extends Dimen {

    public DimenImpl() {
        UIUtils.init(AppBase.app);
    }

    @Override
    public int dp2px(float dpValue) {
        return UIUtils.dp2px(dpValue);
    }

    @Override
    public int px2dp(float pxValue) {
        return UIUtils.px2dp(pxValue);
    }

    @Override
    public int getScreenMinEdge() {
        return UIUtils.getScreenMinEdge(AppBase.app);
    }

    @Override
    public int getScreenHeight() {
        return UIUtils.getScreenHeight(AppBase.app);
    }

    @Override
    public int getScreenWidth() {
        return UIUtils.getScreenWidth(AppBase.app);
    }

    @Override
    public int getStatusBarHeight() {
        return UIUtils.getStatusBarHeight(AppBase.app);
    }
}
