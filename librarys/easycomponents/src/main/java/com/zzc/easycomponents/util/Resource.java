package com.zzc.easycomponents.util;

import com.zuzuche.easycomponents.common.UtilFunction;
import com.zzc.easycomponents.base.IUtil;

/**
 * @author Roye
 * @date 2018/11/13
 */
public abstract class Resource implements IUtil {

    public abstract int getAnim(String name);

    public abstract int getAttr(String name);

    public abstract int getColor(String name);

    public abstract int getDrawable(String name);

    public abstract int getId(String name);

    public abstract int getLayout(String name);

    public abstract int getString(String name);

    public abstract int getStyle(String name);

    public abstract int getMipmap(String name);

    public abstract int getRes(String type, String name);

    @Override
    public String name() {
        return UtilFunction.resource.name();
    }
}
