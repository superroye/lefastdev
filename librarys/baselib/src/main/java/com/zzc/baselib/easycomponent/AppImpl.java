package com.zzc.baselib.easycomponent;

import com.zzc.baselib.util.PackageUtils;
import com.zzc.easycomponents.base.Easys;
import com.zzc.easycomponents.util.App;
import com.zuzuche.easycomponents.common.annotation.EasyUtil;
/**
 * @author Roye
 * @date 2018/11/13
 */
@EasyUtil
public class AppImpl extends App {

    public int versionCode() {
        return PackageUtils.getVersionCode(Easys.getApp());
    }

    @Override
    public String version() {
        return PackageUtils.getVersion(Easys.getApp());
    }

    @Override
    public String packageName() {
        return Easys.getApp().getPackageName();
    }


}
