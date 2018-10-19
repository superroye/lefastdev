package com.zzc.baselib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.zzc.baselib.util.ApplicationUtils;
import com.zzc.baselib.util.SharedPrefUtils;
import com.zzc.baselib.util.SignatureUtils;
import com.zzc.baselib.util.UIUtils;

public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static String DES_KEY = "sealgame_mutil";
    public static String APP_ID;
    public static String APP_UID;
    public static String APP_TOKEN;
    public static String APP_CHANNEL;
    public static String APP_VERSION;
    public static String APP_OS = "2";
    public static boolean APP_DEBUG;
    public static Application app;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SignatureUtils.init(this);
        baseInit();
        registerActivityLifecycleCallbacks(this);
    }

    public void baseInit() {
        APP_ID = String.valueOf(ApplicationUtils.getMetadataInt(this, "appid"));
        SharedPrefUtils.put(LibContext.SP_KEY_APPID, APP_ID);

        setChannel();

        APP_DEBUG = true;
        LibContext.setApp(this, APP_DEBUG);

        setEnv();

        UIUtils.init(this);
    }

    void setChannel() {
        String channel = ApplicationUtils.getMetadataString(this, "UMENG_CHANNEL");
        if (TextUtils.isEmpty(channel)) {
            channel = String.valueOf(ApplicationUtils.getMetadataInt(this, "UMENG_CHANNEL"));
        }
        APP_CHANNEL = channel;
        String channel_sp = SharedPrefUtils.get(LibContext.SP_KEY_CHANNEL);
        if (TextUtils.isEmpty(channel_sp)) {
            SharedPrefUtils.put(LibContext.SP_KEY_CHANNEL, APP_CHANNEL);
        } else {
            APP_CHANNEL = channel_sp;
        }
    }

    void setEnv() {
        String env = SharedPrefUtils.get(LibContext.SP_KEY_ENV);
        if (TextUtils.isEmpty(env)) {
            env = ApplicationUtils.getMetadataString(this, LibContext.SP_KEY_ENV);
        }
        LibContext.setEnv(env);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

}
