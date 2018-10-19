package com.zzc.baselib.ui.widget.recyclerview;

/**
 * @author Roye
 * @date 2018/6/30
 */
public class TalentHolderInfo {

    public int position;
    public Class holderClass;
    public Object itemValue;

    public TalentHolderInfo(Class holderClass, Object itemValue, int position) {
        this.holderClass = holderClass;
        this.itemValue = itemValue;
        this.position = position;
    }
}
