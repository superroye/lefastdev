package com.zzc.easycomponents.util;


import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roye
 * @date 2018/11/9
 */
public class Utils {

    public String[] getSupportUtils() {
        UtilFunction[] utilFunctions = UtilFunction.values();
        if (utilFunctions != null) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < utilFunctions.length; i++) {
                try {
                    IUtil func = UtilsManager.getInstance().getUtil(utilFunctions[i].name());
                    if (func != null) {
                        list.add(utilFunctions[i].name());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return list.toArray(new String[list.size()]);
        }
        return null;
    }

    public static App app() {
        return (App) UtilsManager.getInstance().getUtil(UtilFunction.app.name());
    }

    public static Device device() {
        return (Device) UtilsManager.getInstance().getUtil(UtilFunction.device.name());
    }

    public static Dimen dimen() {
        return (Dimen) UtilsManager.getInstance().getUtil(UtilFunction.dimen.name());
    }

    public static Network network() {
        return (Network) UtilsManager.getInstance().getUtil(UtilFunction.network.name());
    }

    public static Resource resource() {
        return (Resource) UtilsManager.getInstance().getUtil(UtilFunction.resource.name());
    }

    public static Toast toast() {
        return (Toast) UtilsManager.getInstance().getUtil(UtilFunction.toast.name());
    }
}
