package com.zzc.baselib.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * PackageManager,PackageName的相关操作
 * 
 * 
 * @author yy:909012690@lishaoqi
 * @version 创建时间：2014-3-10 下午4:51:32
 */
public class PackageUtils {

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "未知版本";
		}
	}

	/**
	 * 获取版本号(内部识别号)
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 打开应用
	 * 
	 * @param content
	 * @param packageName
	 * @param className
	 */
	public static void startApp(Context content, String packageName, String className) {
		ComponentName mComponentName = new ComponentName(packageName, className);
		Intent i = new Intent();
		if (mComponentName != null) {
			i.setComponent(mComponentName);
		}
		content.startActivity(i);
	}

	/**
	 * 列出所有应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<ResolveInfo> loadAllApps(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		return context.getPackageManager().queryIntentActivities(intent, 0);
	}

	/**
	 * 列出所有应用的信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<ApplicationInfo> listApplicationInfos(Context context) {
		return context.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
	}

	/**
	 * uninstall package normal by system intent
	 * 
	 * @param context
	 * @param packageName
	 *            package name of app
	 * @return whether package name is empty
	 */
	public static boolean uninstall(Context context, String packageName) {
		if (packageName == null || packageName.length() == 0) {
			return false;
		}

		Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:").append(packageName).toString()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}

	public static ApplicationInfo getUninstallApkInfo(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			return appInfo;
		}

		return null;
	}
}
