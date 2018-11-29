package com.zzc.easycomponents.base;

import android.app.Application;

/**
 * @author Roye
 * @date 2018/11/9
 */
public class Easys {

    private static Application mApp;

    public static void init(Application application) {
        mApp = application;
    }

    public static Application getApp() {
        return mApp;
    }
}
