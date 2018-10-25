/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */
package com.zzc.baselib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.zzc.baselib.base.AppBase;

@SuppressLint("DefaultLocale")
public class NetworkUtils {
    public static final String WIFI = "wifi";
    public static final String WIMAX = "wimax";
    // mobile
    public static final String MOBILE = "mobile";
    // 2G network types
    public static final String GSM = "gsm";
    public static final String GPRS = "gprs";
    public static final String EDGE = "edge";
    // 3G network types
    public static final String CDMA = "cdma";
    public static final String UMTS = "umts";
    public static final String HSPA = "hspa";
    public static final String HSUPA = "hsupa";
    public static final String HSDPA = "hsdpa";
    public static final String ONEXRTT = "1xrtt";
    public static final String EHRPD = "ehrpd";
    // 4G network types
    public static final String LTE = "lte";
    public static final String UMB = "umb";
    public static final String HSPA_PLUS = "hspa+";
    // return type
    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_ETHERNET = "ethernet";
    public static final String TYPE_WIFI = "wifi";
    public static final String TYPE_2G = "2g";
    public static final String TYPE_3G = "3g";
    public static final String TYPE_4G = "4g";
    public static final String TYPE_NONE = "none";

    public static String getType() {
        NetworkInfo info = getNetworkInfo();
        return getConnectionInfo(info);
    }

    public static boolean isAvailable() {
        NetworkInfo info = getNetworkInfo();
        if (info != null) {
            return (info != null && info.isConnected());
        } else {
            return false;
        }
    }

    public static boolean isWifi() {
        return getType().equals(TYPE_WIFI);
    }

    public static boolean is2G() {
        return getType().equals(TYPE_2G);
    }

    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager conn = (ConnectivityManager) AppBase.app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return info;
    }

    /**
     * Get the latest network connection information
     *
     * @param info the current active network info
     * @return a JSONObject that represents the network info
     */
    private static String getConnectionInfo(NetworkInfo info) {
        String type = TYPE_NONE;
        if (info != null) {
            // If we are not connected to any network set type to none
            if (!info.isConnected()) {
                type = TYPE_NONE;
            } else {
                type = getType(info);
            }
        }
        Log.d("CordovaNetworkManager", "Connection Type: " + type);
        return type;
    }

    /**
     * Determine the type of connection
     *
     * @param info the network info so we can determine connection type.
     * @return the type of mobile network we are on
     */
    private static String getType(NetworkInfo info) {
        if (info != null) {
            String type = info.getTypeName();

            if (type.toLowerCase().equals(WIFI)) {
                return TYPE_WIFI;
            } else if (type.toLowerCase().equals(MOBILE)) {
                type = info.getSubtypeName();
                if (type.toLowerCase().equals(GSM) || type.toLowerCase().equals(GPRS)
                        || type.toLowerCase().equals(EDGE)) {
                    return TYPE_2G;
                } else if (type.toLowerCase().startsWith(CDMA) || type.toLowerCase().equals(UMTS)
                        || type.toLowerCase().equals(ONEXRTT) || type.toLowerCase().equals(EHRPD)
                        || type.toLowerCase().equals(HSUPA) || type.toLowerCase().equals(HSDPA)
                        || type.toLowerCase().equals(HSPA)) {
                    return TYPE_3G;
                } else if (type.toLowerCase().equals(LTE) || type.toLowerCase().equals(UMB)
                        || type.toLowerCase().equals(HSPA_PLUS)) {
                    return TYPE_4G;
                }
            }
        } else {
            return TYPE_NONE;
        }
        return TYPE_UNKNOWN;
    }
}
