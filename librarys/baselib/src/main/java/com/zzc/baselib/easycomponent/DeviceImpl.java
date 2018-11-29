package com.zzc.baselib.easycomponent;

import android.os.Build;

import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import com.zzc.baselib.util.DeviceUtils;
import com.zzc.easycomponents.base.Easys;
import com.zzc.easycomponents.util.Device;

/**
 * @author Roye
 * @date 2018/11/13
 */
@EasyUtil
public class DeviceImpl extends Device {

    @Override
    public String imei() {
        return DeviceUtils.getIMEI(Easys.getApp());
    }

    @Override
    public String brand() {
        return Build.BRAND;
    }

    @Override
    public String model() {
        return Build.MODEL;
    }

    @Override
    public String device() {
        return Build.DEVICE;
    }

    @Override
    public String product() {
        return Build.PRODUCT;
    }

    @Override
    public String display() {
        return Build.DISPLAY;
    }

    @Override
    public String manufacture() {
        return Build.MANUFACTURER;
    }
}
