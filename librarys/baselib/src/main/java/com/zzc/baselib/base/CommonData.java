package com.zzc.baselib.base;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonData {
    public String mSpFileName;

    public CommonData(String fileName) {
        mSpFileName = fileName;
    }

    private SharedPreferences getSp() {
        return LibContext.getApp().getSharedPreferences(mSpFileName, Context.MODE_PRIVATE);
    }

    public void setValue(String key, String value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        if (value == null) {
            editor.remove(key);
        } else {
            editor.putString(key, value);
        }

        editor.commit();
    }

    public void setValue(String key, int value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setValue(String key, long value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void setValue(String key, boolean value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public long getLongValue(String key) {
        SharedPreferences sp = getSp();
        return sp.getLong(key, 0);
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sp = getSp();
        return sp.getBoolean(key, false);
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        SharedPreferences sp = getSp();
        return sp.getBoolean(key, defaultValue);
    }

    public int getIntValue(String key) {
        SharedPreferences sp = getSp();
        return sp.getInt(key, 0);
    }

    public String getValue(String key) {
        return getValue(key, "");
    }

    public String getValue(String key, String defaultValue) {
        SharedPreferences sp = getSp();
        return sp.getString(key, defaultValue);
    }

    public void clear() {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}
