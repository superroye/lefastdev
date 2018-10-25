package com.zzc.baselib.ui.viewtools;

import android.view.View;

/**
 * Created by Roye on 2018/6/6.
 */
public class RepeatClickChecker {

    private static long clickTime = 0;
    private static int viewHashcode = 0;

    public static boolean invalidClick(View view) {
        long current = System.currentTimeMillis();

        if (view.hashCode() != viewHashcode) {
            viewHashcode = view.hashCode();
            clickTime = current;
            return false;
        }
        if (current - clickTime < 1000) {
            clickTime = current;
            return true;
        } else {
            clickTime = current;
            return false;
        }
    }
}
