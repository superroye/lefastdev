package com.zzc.easycomponents.util;

import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

/**
 * @author Roye
 * @date 2018/11/13
 */
public abstract class Network implements IUtil {

    public abstract String getType();

    public abstract boolean isAvailable();

    public abstract boolean isWifi();

    public abstract boolean is2G();

    @Override
    public String name() {
        return UtilFunction.network.name();
    }
}
