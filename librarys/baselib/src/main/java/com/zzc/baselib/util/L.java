package com.zzc.baselib.util;

import android.util.Log;

/**
 * Created by Roye on 2018/4/3.
 */

public class L {

    private static final String TAG = "Log";
    private static boolean isDebug = true;

    public static void setDebug(boolean isDebug) {
        L.isDebug = isDebug;
    }

    public static int v(String tag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(tag, msg, tr);
    }

    public static int e(String tag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(tag, msg, tr);
    }

    /**
     * default tag
     */
    public static int v(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(TAG, msg);
    }

    public static int v(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(TAG, msg, tr);
    }

    public static int d(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(TAG, msg);
    }

    public static int d(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(TAG, msg, tr);
    }

    public static int i(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(TAG, msg);
    }

    public static int i(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(TAG, msg, tr);
    }

    public static int w(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(TAG, msg);
    }

    public static int w(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(TAG, msg, tr);
    }

    public static int e(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(TAG, msg);
    }

    public static int e(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(TAG, msg, tr);
    }
}
