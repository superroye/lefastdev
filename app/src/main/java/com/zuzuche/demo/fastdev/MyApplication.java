package com.zuzuche.demo.fastdev;

import com.zzc.baselib.base.AppBase;
import com.zzc.baselib.base.BaseApplication;

/**
 * @author Roye
 * @date 2018/10/25
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        AppBase.setDebug(true);
    }
}
