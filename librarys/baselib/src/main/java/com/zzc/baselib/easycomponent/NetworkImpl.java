package com.zzc.baselib.easycomponent;

import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import com.zzc.baselib.util.NetworkUtils;
import com.zzc.easycomponents.util.Network;

/**
 * @author Roye
 * @date 2018/11/13
 */
@EasyUtil
public class NetworkImpl extends Network {

    @Override
    public boolean isAvailable() {
        return NetworkUtils.isAvailable();
    }

    @Override
    public boolean is2G() {
        return NetworkUtils.is2G();
    }

    @Override
    public boolean isWifi() {
        return NetworkUtils.isWifi();
    }

    @Override
    public String getType() {
        return NetworkUtils.getType();
    }
}