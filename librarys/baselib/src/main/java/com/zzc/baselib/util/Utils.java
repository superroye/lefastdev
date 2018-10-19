package com.zzc.baselib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static final Pattern p = Pattern.compile("^1(3|4|5|7|8|9)[0-9]{9}$");

	public static boolean isPwdValid(String pwd) {
		String PASSWORD_PATTERN = "^(?=\\S+$).{6,12}$";
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		return pattern.matcher(pwd).matches();
	}

    public static boolean isMobileValid(String mobile) {
        Pattern p = Pattern.compile("^1(3|4|5|7|8|9)[0-9]{9}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public static String getVersionCode(Context context) {
		PackageInfo packInfo;
		try {
			packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return "" + packInfo.versionCode;
		} catch (NameNotFoundException e) {
		}
		return "0";
	}

	public static String getVersionName(Context context) {
		PackageInfo packInfo;
		try {
			packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return "" + packInfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return "1.0.0";
	}

	public static String getUmengChannel(Context context) {
		String channel = "";
		if (context == null)
			return channel;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			channel = appInfo.metaData.getString("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return channel;
	}

	public static void makeViewHeightStretch(View view) {
		LayoutParams lp2 = (LayoutParams) view.getLayoutParams();
		lp2.weight = 1;
		lp2.height = 0;
	}

	public static void makeViewHeightWrap(View view) {
		LayoutParams lp1 = (LayoutParams) view.getLayoutParams();
		lp1.weight = 0;
		lp1.height = LayoutParams.WRAP_CONTENT;
	}

	public static boolean isPhoneNumberValid(String num) {
		Matcher m = p.matcher(num);
		return m.matches();
	}

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 不可访问返回 -1
	 * 
	 * @param context
	 * @return
	 */
	public static int isContactReadable(Context context) {
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(Phone.CONTENT_URI, new String[] { Contacts.DISPLAY_NAME, Phone.NUMBER }, "in_visible_group=1", null, null);
			if (cursor == null)
				return -1;

			return cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return 0;
	}

	public static boolean isMeizu() {
		return Build.PRODUCT.toLowerCase().contains("meizu");
	}

	public static boolean isXiaomi() {
		return Build.BRAND.toLowerCase().contains("xiaomi");
	}

	public static boolean hasSmartBar() {
		try {
			boolean bool2 = ((Boolean) Class.forName(Build.class.getName()).getMethod("hasSmartBar", new Class[0]).invoke(null, new Object[0])).booleanValue();
			return bool2;
		} catch (Exception localException) {
			boolean bool1;
			if ((Build.DEVICE.equals("mx")) || (Build.DEVICE.equals("m9")))
				bool1 = false;
			else if (Build.DEVICE.startsWith("mx"))
				bool1 = true;
			else
				bool1 = false;
			return bool1;
		}
	}

	// 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	public static boolean hasBind(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String flag = sp.getString("bind_flag", "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}

	public static String[] getBindId(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return new String[] { sp.getString("bind_userId", ""), sp.getString("bind_channelId", "") };
	}

	public static void setBind(Context context, String userId, String channelId, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		editor.putString("bind_userId", userId);
		editor.putString("bind_channelId", channelId);
		editor.commit();
	}

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (activityManager == null)
			return false;

		try {
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
			if (tasksInfo.size() > 0) {
				// 应用程序位于堆栈的顶层
				String packageName = context.getPackageName();
				if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
					return true;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getPhoneCount(Context context) {
		int count = 0;
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(Phone.CONTENT_URI, new String[] { Contacts.DISPLAY_NAME, Phone.NUMBER }, null, null, null);
			if (cursor != null) {
				count = cursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return count;
	}

	public static boolean isLocationEnabled(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gps_enabled || network_enabled;
	}

	// 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
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

	public static String makeSpaceNumber(String showNumber) {
		if (!TextUtils.isEmpty(showNumber) && showNumber.length() == 11) {
			showNumber = showNumber.substring(0, 3) + " " + showNumber.substring(3, 7) + " " + showNumber.substring(7, 11);
			return showNumber;
		}
		return null;
	}

	public static String shortNumber(int number) {
		if (number >= 1000) {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			double d = number / 1000.0f;
			String format = nf.format(d) + "k";
			if (format.indexOf(".") == -1) {
				format += "+";
			}
			return format;
		} else {
			return number + "";
		}
	}

	public static File savePath(Context context, String type) {
		if (type == null)
			type = "";
		File path = context.getExternalFilesDir(type);
		if (path == null || !path.exists()) {
			path = new File(context.getCacheDir(), type);
		}
		return path;
	}

	public static void recycleImageView(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof BitmapDrawable) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				if (bitmapDrawable != null) {
					Bitmap bitmap = bitmapDrawable.getBitmap();
					if (bitmap != null && !bitmap.isRecycled())
						bitmap.recycle();
				}
				imageView.setImageBitmap(null);
			}
		}
	}

	public static Uri toUri(Context context, int resId) {
		Resources resources = context.getResources();
		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/'
				+ resources.getResourceEntryName(resId));
	}

	public static boolean isServiceRun(Context mContext, String className) {
		boolean isRun = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
		if (serviceList == null)
			return isRun;
		int size = serviceList.size();
		for (int i = 0; i < size; i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRun = true;
				break;
			}
		}
		return isRun;
	}

	public static void setAlarm(Context context, Intent intent, long time) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(pendingIntent);
		// if(BuildConfig.DEBUG){
		// Toast.makeText(context, "全民提醒: " + new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)),
		// Toast.LENGTH_SHORT).show();
		// }
		am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static boolean checkActivityState(Activity activity) {
		if (activity != null) {

			if (activity.isFinishing())
				return false;

			if (Build.VERSION.SDK_INT >= 17)
				if (activity.isDestroyed())
					return false;
			return true;
		}
		return false;
	}

	/**
	 * 设置当前Activity屏幕亮度，此方法不会影响系统屏幕亮度设置
	 *
	 * @param window
	 *            Activity当前运行的Window引用
	 * @param brightness
	 *            屏幕亮度(0 -- 255)
	 * @return 设置屏幕亮度是否成功
	 */
	public static boolean setBrightness(Window window, int brightness) {
		if (brightness < 0 || brightness > 255) {
			return false;
		}

		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightness / 255.0f;
		window.setAttributes(lp);

		return true;
	}

	/**
	 * 获得当前系统屏幕亮度
	 *
	 * @param context
	 *            Activity当前运行的Window引用
	 * @return 当前屏幕亮度(0 -- 255)
	 */
	public static int getSystemBrightness(Context context) {
		// 获取当前亮度,获取失败则返回255
		return (android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255));
	}

	/**
	 * 获得当前Activity屏幕亮度
	 *
	 * @param window Activity当前运行的Window引用
	 * @return 当前屏幕亮度(0 -- 255)
	 */
	public static int getWindowBrightness(Window window) {
		WindowManager.LayoutParams lp = window.getAttributes();
		return Math.round(lp.screenBrightness * 255.0f);
	}
}
