package com.zzc.easycomponents.util;

import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

/**
 * @author Roye
 * @date 2018/11/9
 */
public abstract class App implements IUtil {

    public abstract int versionCode();

    public abstract String version();

    public abstract String packageName();

    @Override
    public final String name() {
        return UtilFunction.app.name();
    }

}
