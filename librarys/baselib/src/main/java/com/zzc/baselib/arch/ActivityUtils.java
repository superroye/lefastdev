package com.zzc.baselib.arch;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Roye
 * @date 2018/8/23
 */
public class ActivityUtils {

    public static FragmentActivity getActivity(Context context) {
        Context context1 = context;
        while (!(context1 instanceof Activity) && context1 instanceof ContextWrapper) {
            context1 = ((ContextWrapper) context1).getBaseContext();
        }

        AppCompatActivity activity = null;
        if (context1 instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context1;
        }
        if (activity == null) {
            android.util.Log.e("ActivityUtils", "current activity is null");
        }
        return activity;
    }
}
