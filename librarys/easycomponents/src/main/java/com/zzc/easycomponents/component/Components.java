package com.zzc.easycomponents.component;

/**
 * @author Roye
 * @date 2018/11/9
 */
public class Components {

    private static ComponentManager mManager;

    public static ComponentManager manager() {
        if (mManager == null) {
            mManager = new ComponentManager();
        }
        return mManager;
    }

    public static NetworkComponent network() {
        return manager().networkComponent();
    }


}
