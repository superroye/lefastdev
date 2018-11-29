package com.zzc.baselib.easycomponent;

import com.zuzuche.easycomponents.common.annotation.EasyUtil;
import com.zzc.baselib.util.ResourceUtils;
import com.zzc.easycomponents.util.Resource;

/**
 * @author Roye
 * @date 2018/11/13
 */
@EasyUtil
public class ResourceImpl extends Resource {

    @Override
    public int getAnim(String name) {
        return ResourceUtils.getAnim(name);
    }

    @Override
    public int getAttr(String name) {
        return ResourceUtils.getAttr(name);
    }

    @Override
    public int getColor(String name) {
        return ResourceUtils.getColor(name);
    }

    @Override
    public int getDrawable(String name) {
        return ResourceUtils.getDrawable(name);
    }

    @Override
    public int getId(String name) {
        return ResourceUtils.getId(name);
    }

    @Override
    public int getLayout(String name) {
        return ResourceUtils.getLayout(name);
    }

    @Override
    public int getString(String name) {
        return ResourceUtils.getString(name);
    }

    @Override
    public int getStyle(String name) {
        return ResourceUtils.getStyle(name);
    }

    @Override
    public int getMipmap(String name) {
        return ResourceUtils.getMipmap(name);
    }

    @Override
    public int getRes(String type, String name) {
        return ResourceUtils.getRes(type, name);
    }
}
