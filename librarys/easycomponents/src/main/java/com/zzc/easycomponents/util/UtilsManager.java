package com.zzc.easycomponents.util;

import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import com.zzc.easycomponents.base.IUtil;
import com.zzc.easycomponents.support.ComponentRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Roye
 * @date 2018/11/13
 */
public class UtilsManager {

    private HashMap<String, IUtil> utilMap;
    private static UtilsManager mInstance;

    public static UtilsManager getInstance() {
        if (mInstance == null) {
            synchronized (UtilsManager.class) {
                if (mInstance == null) {
                    mInstance = new UtilsManager();
                    mInstance.utilMap = new HashMap<>(32);
                    tryAutoInit();
                }
            }
        }

        return mInstance;
    }

    private static void tryAutoInit() {
        try {
            Class cls;
            cls = Class.forName("com.zzc.easycomponents.util.UtilAutoIniter");
            Method method;
            method = cls.getMethod("init");
            method.invoke(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void init(IUtil util) {
        String name = util.name();
        int price = getUtilPrice(util);

        IUtil old = utilMap.get(name);
        if (old == null || getUtilPrice(old) < price) {
            utilMap.put(name, util);
        }
    }

    private int getUtilPrice(IUtil util) {
        EasyUtil easyUtil = util.getClass().getAnnotation(EasyUtil.class);
        if (easyUtil != null) {
            return easyUtil.price();
        }
        return 0;
    }

    public IUtil getUtil(String name) {
        IUtil util = utilMap.get(name);
        if (util == null) {
            throw new ComponentRuntimeException(name + " util has not initialized");
        }
        return util;
    }

}
