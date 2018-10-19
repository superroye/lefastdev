package com.zzc.baselib.util;

/**
 * Created by Roye on 2018/4/8.
 */

public class T {

    public static int parseIntDef(String val) {
        int def = 0;
        try {
            def = Integer.parseInt(val);
        } catch (Exception e) {
            def = 0;
        }
        return def;
    }

    public static int parseInt(String val, int defInt) {
        int def = defInt;
        try {
            def = Integer.parseInt(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public static long parseLongDef(String val) {
        long def = 0;
        try {
            def = Long.parseLong(val);
        } catch (Exception e) {
            def = 0;
        }
        return def;
    }

    public static int aroundInt(float val) {
        return new java.math.BigDecimal(val).setScale(0, java.math.BigDecimal.ROUND_HALF_UP).intValue();
    }
}
