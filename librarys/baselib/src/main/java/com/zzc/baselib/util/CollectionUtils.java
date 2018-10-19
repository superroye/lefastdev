package com.zzc.baselib.util;

import java.util.Collection;

/**
 * Created by Roye on 2017/7/1.
 */

public class CollectionUtils {

    public static boolean isEmpty(Collection list) {
        return list == null || list.isEmpty();
    }

    public static boolean has(int target, int... vals) {
        if (vals == null) {
            return true;
        }
        for (int i = 0; i < vals.length; i++) {
            if (target == vals[i]) {
                return true;
            }
        }
        return false;
    }

}
