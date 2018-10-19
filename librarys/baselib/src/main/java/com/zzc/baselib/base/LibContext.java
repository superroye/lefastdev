package com.zzc.baselib.base;

import android.app.Application;

import com.zzc.baselib.util.L;
import com.zzc.baselib.util.SharedPrefUtils;

/**
 * Created by Roye on 2018/4/2.
 */

public class LibContext {

    public static final String TAG = "baselib";
    public static final String SP_KEY_APPID = "appId";
    public static final String SP_KEY_CHANNEL = "channel";
    public static final String SP_KEY_ENV = "env";
    public static String ENV;
    public static boolean DEBUG;
    public static boolean PRODUCT_ENV;
    private static Application app;

    public static void setApp(Application app, boolean isDebug) {
        LibContext.app = app;
        DEBUG = isDebug;
        L.setDebug(isDebug);
    }

    public static Application getApp() {
        if (app == null) {
            throw new NullPointerException("LibContext.app is not inited!");
        }
        return app;
    }

    public static void setEnv(String test) {
        if (test != null) {
            ENV = test;
        } else {
            ENV = "test";
        }

        PRODUCT_ENV = !"test".equals(ENV);

        SharedPrefUtils.put(SP_KEY_ENV, ENV);
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }


}
