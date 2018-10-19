package com.zzc.baselib.ui.widget.statusbar;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zzc.baselib.base.BaseApplication;
import com.zzc.baselib.util.UIUtils;
import com.zzc.baselib.R;
import com.zzc.baselib.base.BaseApplication;
import com.zzc.baselib.util.UIUtils;

/**
 * @author Roye
 * @date 2018/8/16
 */
public class AppCompatStatusBar {

    private static final int STATE_EXPAND = 1;
    private static final int STATE_COLLAPSE = 2;
    private static final int STATE_TEMP = 3;

    private int state = STATE_EXPAND;

    private int statusBarHeight;
    private AppCompatActivity mActivity;

    public AppCompatStatusBar(Context context) {
        try {
            init(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context) throws Exception {
        Context context1 = context;
        while (!(context1 instanceof Activity) && context1 instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (context1 instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context1;
        }
        if (mActivity == null) {
            throw new Exception("current activity is null");
        }
        statusBarHeight = UIUtils.getStatusBarHeight(context);
    }

    public void onScrollY(int scrollY) {
        Activity activity = mActivity;
        float minAlpha = 0.4f;
        if (scrollY >= statusBarHeight) {
            if (state != STATE_COLLAPSE) {
                addStatusViewWithColor(activity, ContextCompat.getColor(BaseApplication.app, R.color.colorStatusbar));
                state = STATE_COLLAPSE;
            }
        } else if (scrollY == 0) {
            addStatusViewWithColor(activity,
                    UIUtils.getColorWithAlpha(minAlpha, ContextCompat.getColor(BaseApplication.app, R.color.colorStatusbar)));
            state = STATE_EXPAND;
        } else {
            if (state != STATE_TEMP) {
                state = STATE_TEMP;
            }

            float alpha = minAlpha + (1f - minAlpha) * scrollY / (float) statusBarHeight;

            addStatusViewWithColor(activity,
                    UIUtils.getColorWithAlpha(alpha, ContextCompat.getColor(BaseApplication.app, R.color.colorStatusbar)));
        }
    }

    //为了适应同一个window不同fragment情况，失去焦点马上重置
    public void resetState() {
        state = STATE_EXPAND;
    }

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //直接设置状态栏颜色
                activity.getWindow().setStatusBarColor(color);
            } else {
                //增加占位状态栏
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                int viewId = R.id.statusbarutil_fake_status_bar_view;
                View statusBarView = (View) decorView.getTag(viewId);
                if (statusBarView == null) {
                    statusBarView = new View(activity);
                    statusBarView.setId(viewId);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
                    decorView.setTag(viewId, statusBarView);
                    decorView.addView(statusBarView, lp);
                }
                statusBarView.setBackgroundColor(color);
            }
        }
    }

    public void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = mActivity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
                //window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = mActivity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                //attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    public void clearActionBarShadow() {
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar supportActionBar = mActivity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setElevation(0);
            }
        }
    }
}
