package com.zuzuche.demo.fastdev;

import com.zzc.baselib.base.AppBase;
import com.zzc.baselib.base.BaseApplication;
import com.zzc.baselib.easycomponent.UtilsInitialize;
import com.zzc.easycomponents.base.Easys;
import com.zzc.easycomponents.component.Components;
import com.zzc.easycomponents.util.Utils;
import com.zzc.network.easycomponent.NetworkComponentImpl;

/**
 * @author Roye
 * @date 2018/10/25
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        AppBase.setDebug(true);

        initEasyComponent();
    }

    void initEasyComponent() {
        Easys.init(this); //初始化上下文

        UtilsInitialize.init(); //初始化工具类

        Components.manager().init(new NetworkComponentImpl()); //初始化网络组件
    }
}
