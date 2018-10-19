package com.zzc.baselib.ui.viewtools;

/**
 * Created by Roye on 2018/6/6.
 */
public class RepeatClickChecker {

    static long clickTime = 0;

    public static boolean invalidClick() {
        long current = System.currentTimeMillis();
        if (current - clickTime < 1000) {
            clickTime = current;
            return true;
        } else {
            clickTime = current;
            return false;
        }
    }
}
