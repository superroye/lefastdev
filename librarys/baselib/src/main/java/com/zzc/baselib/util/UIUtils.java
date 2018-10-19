package com.zzc.baselib.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class UIUtils {

    // 当前屏幕的densityDpi
    public static float densityDpi = 0.0f;
    // 密度因子
    public static float scale = 0.0f;

    public static int screenWidth, screenHeight;

    public static void init(Context context) {
        if (context == null)
            return;

        Resources resources = context.getResources();
        if (resources == null)
            return;

        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (displayMetrics == null)
            return;

        densityDpi = displayMetrics.densityDpi;
        // 密度因子
        scale = densityDpi / 160;

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    /**
     * 密度转换像素
     * */
    public static int dip2px(float dipValue) {
        int px = (int) (dipValue * scale + 0.5f);
        return px;
    }

    /**
     * 像素转换密度
     * */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

	/**
	 * 
	 * 描述：dip转换为px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 * @throws
	 */
	public static int dpToPx(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 描述：dip转换为px
	 * 
	 * @param res
	 * @param dp
	 * @return
	 */
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	public static float pxToDp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return px / scale;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenMinEdge(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return Math.min(dm.widthPixels, dm.heightPixels);
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

    /**
     * 状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if(context == null)
            return 0;

        Resources resources = context.getResources();
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 对rgb色彩加入透明度
     *
     * @param alpha     透明度，取值范围 0.0f -- 1.0f.
     * @param baseColor
     * @return a color with alpha made from base color
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
