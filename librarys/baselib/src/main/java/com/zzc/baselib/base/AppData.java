package com.zzc.baselib.base;

import android.text.TextUtils;

import com.zzc.baselib.util.DeviceUtils;

/**
 * Created by Roye on 2018/5/23.
 */
public class AppData {

    public final static String SP_FILE = "app_data";
    private static final String SP_KEY_UID = "uid";
    private static final String SP_KEY_TOKEN = "token";
    private static final String SP_KEY_ACCESS_TOKEN = "accessToken";
    private static final String SP_KEY_USERFACE = "userFace";
    private static final String SP_KEY_USERNICK = "userNick";
    private static final String SP_KEY_UUID = "uuid";
    private static final String SP_KEY_ACCOUNT_TYPE = "account_type"; //1 正式 2 游客

    private static CommonData data;

    private static CommonData getData() {
        if (data == null) {
            data = new CommonData(SP_FILE);
        }
        return data;
    }

    public static String getUuid() {
        String uuid = getData().getValue(SP_KEY_UUID);
        if (TextUtils.isEmpty(uuid)) {
            uuid = DeviceUtils.deviceUUId(LibContext.getApp());
            getData().setValue(SP_KEY_UUID, uuid);
        }

        return uuid;
    }

    public static String getUid() {
        return getData().getValue(SP_KEY_UID);
    }

    public static void setUid(String uid) {
        getData().setValue(SP_KEY_UID, uid);
    }

    public static String getToken() {
        return getData().getValue(SP_KEY_TOKEN);
    }

    public static void setToken(String token) {
        getData().setValue(SP_KEY_TOKEN, token);
    }

    public static String getNick() {
        return getData().getValue(SP_KEY_USERNICK);
    }

    public static void setNick(String nick) {
        getData().setValue(SP_KEY_USERNICK, nick);
    }

    public static String getHead() {
        return getData().getValue(SP_KEY_USERFACE);
    }

    public static void setHead(String head) {
        getData().setValue(SP_KEY_USERFACE, head);
    }

    public static String getAccessToken() {
        return getData().getValue(SP_KEY_ACCESS_TOKEN);
    }

    public static void setAccessToken(String accessToken) {
        getData().setValue(SP_KEY_ACCESS_TOKEN, accessToken);
    }

    public static int getAccountType() {
        return getData().getIntValue(SP_KEY_ACCOUNT_TYPE);
    }

    public static void setAccountType(int accountType) {
        getData().setValue(SP_KEY_ACCOUNT_TYPE, accountType);
    }


    public static void clear() {
        getData().clear();
    }
}
