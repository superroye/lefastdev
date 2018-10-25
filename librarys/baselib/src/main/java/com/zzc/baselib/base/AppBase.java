package com.zzc.baselib.base;

import android.app.Application;
import android.text.TextUtils;

import com.zzc.baselib.util.ApplicationUtils;
import com.zzc.baselib.util.L;
import com.zzc.baselib.util.SharedPrefUtils;
import com.zzc.baselib.util.UIUtils;

/**
 * @author Roye
 * @date 2018/10/24
 */
public class AppBase {

    public static String APP_ID;
    public static String APP_CHANNEL;
    public static Application app;
    public static String ENV;
    public static boolean DEBUG;
    public static boolean PRODUCT_ENV;

    public static final String SP_KEY_APPID = "appId";
    public static final String SP_KEY_CHANNEL = "app_channel";
    public static final String SP_KEY_ENV = "env";
    public static final String ENV_TEST = "test";
    public static final String ENV_PRODUCT = "product";

    public static void init(Application app, boolean isDebug) {
        AppBase.app = app;
        DEBUG = isDebug;

        L.setDebug(isDebug);

        APP_ID = String.valueOf(ApplicationUtils.getMetadataInt(app, SP_KEY_APPID));
        SharedPrefUtils.put(SP_KEY_APPID, APP_ID);

        initChannel();

        initEnv();

        UIUtils.init(app);
    }

    private static void initChannel() {
        String channel = ApplicationUtils.getMetadataString(app, SP_KEY_CHANNEL);
        if (TextUtils.isEmpty(channel)) {
            channel = String.valueOf(ApplicationUtils.getMetadataInt(app, SP_KEY_CHANNEL));
        }
        APP_CHANNEL = channel;
        String channel_sp = SharedPrefUtils.get(SP_KEY_CHANNEL);
        if (TextUtils.isEmpty(channel_sp)) {
            SharedPrefUtils.put(SP_KEY_CHANNEL, APP_CHANNEL);
        } else {
            APP_CHANNEL = channel_sp;
        }
    }

    private static void initEnv() {
        String env = SharedPrefUtils.get(SP_KEY_ENV);
        if (TextUtils.isEmpty(env)) {
            env = ApplicationUtils.getMetadataString(app, SP_KEY_ENV);
        }

        setEnv(env);
    }

    public static void setEnv(String env) {
        if (env != null) {
            ENV = env;
        } else {
            ENV = ENV_TEST;
        }

        PRODUCT_ENV = !ENV_TEST.equals(ENV);

        SharedPrefUtils.put(SP_KEY_ENV, ENV);
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }
}
