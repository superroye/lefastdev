package com.zzc.easycomponents.util;


import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

/**
 * @author Roye
 * @date 2018/11/9
 */
public abstract class Device implements IUtil {

    public abstract String imei();
    public abstract String brand();
    public abstract String model();
    public abstract String device();
    public abstract String product();
    public abstract String display();
    public abstract String manufacture();

    @Override
    public String name() {
        return UtilFunction.device.name();
    }
}
