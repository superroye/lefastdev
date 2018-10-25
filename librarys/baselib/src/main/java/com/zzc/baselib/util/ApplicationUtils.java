package com.zzc.baselib.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.os.Bundle;

import com.zzc.baselib.base.AppBase;

import java.util.List;

/**
 * App相关操作，AndroidManifest.xml相关操作
 * 
 * @author yy:909012690@lishaoqi
 * @version 创建时间：2014-8-28 下午2:34:37
 */
public class ApplicationUtils {

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(256);
        if (myList == null || myList.isEmpty()) {
            return false;
        }
        try {
            for (int i = 0; i < myList.size(); i++) {
                String mName = myList.get(i).service.getClassName().toString();
                if (mName.equals(serviceName)) {
                    isWork = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isWork;
    }

    @Deprecated
	public static boolean isRunningOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if (activityManager == null)
			return false;

		String packageName = context.getPackageName();

		boolean running = false;

		try {
			List<RunningTaskInfo> tasksInfo = activityManager
					.getRunningTasks(1);
			if (tasksInfo.size() > 0) {
				if (packageName.equals(tasksInfo.get(0).topActivity
						.getPackageName())) {
					running = true;
				}
			}
		} catch (SecurityException e) {
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.processName.equals(context.getPackageName())) {
					if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
						running = true;
						break;
					}
				}
			}
		}
		// LogUtils.log(packageName, "isRunningOnForeground", running);
		return running;
	}

	public static boolean isLocationEnabled(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gps_enabled || network_enabled;
	}

	public static String getMetadataString(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (ai.metaData != null) {
				return ai.metaData.getString(key);
			}
		} catch (Throwable e) {
			// if we can't find it in the manifest, just return null
			e.printStackTrace();
		}
		return null;
	}

	public static int getMetadataInt(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (ai.metaData != null) {
				return ai.metaData.getInt(key);
			}
		} catch (Throwable e) {
			// if we can't find it in the manifest, just return null
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 获取应用的签名
	 * 
	 * @param packageName
	 * @return
	 */
	public static String getSign(String packageName) {
		PackageManager pm = AppBase.app.getPackageManager();
		PackageInfo info;
		try {
			info = pm
					.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			if (info != null) {
				return info.signatures[0].toCharsString();
			} else {
				return null;
			}
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static boolean hasInstall(String packageName) {
		String sign = getSign(packageName);
		return sign != null;
	}

	public static void run(String packageName, String dataKey, Bundle data) {
		if (AppBase.app != null) {
			PackageManager packageManager = AppBase.app.getPackageManager();
			if (packageManager != null) {
				Intent intent = packageManager
						.getLaunchIntentForPackage(packageName);
				if (intent != null) {
					if (data != null && dataKey != null) {
						intent.putExtra(dataKey, data);
					}
					AppBase.app.startActivity(intent);
				}
			}
		}
	}

}
