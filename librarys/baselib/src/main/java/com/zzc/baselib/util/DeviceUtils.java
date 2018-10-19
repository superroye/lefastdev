package com.zzc.baselib.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.UUID;

public class DeviceUtils {

    public static String uniqueId(Context context) {
        return UUID.nameUUIDFromBytes((getInfo(context) + getIMEI(context) + getAndroidId(context)).getBytes()).toString();
    }

    public static String deviceUUId(Context context) {
        return MD5.toMd5(getInfo(context) + getIMEI(context) + getAndroidId(context));
    }

    public static String getInfo(Context context) {
        String model = Build.MODEL;
        String device = Build.DEVICE;
        String brand = Build.BRAND;
        String product = Build.PRODUCT;
        String display = Build.DISPLAY;
        String manufacture = Build.MANUFACTURER;

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;

        StringBuilder sb = new StringBuilder();
        String finalInfo = sb.append("MODEL " + model).append("\nDEVICE " + device).append("\nBRAND " + brand).append("\nPRODUCT " + product).append("\nDISPLAY " + display)
                .append("\nMANUFACTURE " + manufacture).append("\nSCREEN_WIDTH " + screenWidth).append("\nSCREEN_HEIGHT " + screenHeight).append("\nDENSITY " + density).toString();
        return finalInfo;
    }

    public static final String getBluetoothMac() {
        BluetoothAdapter adapter = null;
        String bluetoothMac = null;
        try {
            adapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothMac = adapter.getAddress();
        } catch (Exception e) {
        }
        return bluetoothMac;
    }

    public static final String getWlanMac(Context context) {
        String wlanMac = null;
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wlanMac = wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {

        }
        return wlanMac;
    }

    public static final String getAndroidId(Context context) {
        String androidID = null;
        try {
            androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return androidID;
    }

    @SuppressLint("MissingPermission")
    public static final String getIMEI(Context context) {
        String deviceIMEI = null;
        try {
            TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceIMEI = teleManager.getDeviceId();
        } catch (Exception e) {

        }
        return deviceIMEI;
    }

    public static float getScreenInches(Context context) {
        float screenInches = -1;
        try {
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            double width = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double height = Math.pow(dm.heightPixels / dm.ydpi, 2);
            screenInches = (float) (Math.sqrt(width + height));
        } catch (Exception e) {
        }
        return screenInches;
    }


    /**
     * 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {

        }
        return false;
    }

    public static int dp2px(Context context, int dip) {
        Resources resources = context.getResources();
        int px = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics()));
        return px;
    }

    public static int px2dp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        int px = Math.round(sp * scale);
        return px;
    }
}
