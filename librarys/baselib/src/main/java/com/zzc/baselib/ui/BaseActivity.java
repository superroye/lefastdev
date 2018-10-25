package com.zzc.baselib.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzc.baselib.BuildConfig;
import com.zzc.baselib.ui.listener.IProgressDialog;
import com.zzc.baselib.ui.widget.statusbar.AppCompatStatusBar;
import com.zzc.baselib.util.UIUtils;
import com.zzc.baselib.ui.widget.statusbar.StatusBarUtils;

public class BaseActivity extends AppCompatActivity implements IProgressDialog {

    private boolean isLoadExitAnim = true;
    private boolean isLoadEnterAnim = true;

    private AppCompatStatusBar appCompatStatusBar;

    public BaseActivity getActivity() {
        return this;
    }

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (layoutId() != 0) {
            setContentView(layoutId());
        }
        initView();
        initFlag();
    }

    /**
     * 设置环境标志
     */
    private void initFlag() {

        if (BuildConfig.DEBUG) {
            String cookies = CookieManager.getInstance().getCookie(".easyrentcars.com");
            if (!TextUtils.isEmpty(cookies)) {
                int index = cookies.indexOf("_branch_=");
                if (index == -1) {
                    return;
                }
                int index2 = cookies.indexOf(";", index);
                cookies = cookies.substring(index + "_branch_=".length(), index2);
            }
            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
            textView.setText(cookies);
            textView.setTextSize(12);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // params.topMargin = StatusBarUtils.getStatusBarHeight(this);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            addContentView(textView, params);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initData();
    }

    protected void setStatusBar(@ColorInt int color) {
        StatusBarUtils.setColor(this, color, 0);
    }

    protected void setStatusBar() {
        setStatusBar(ContextCompat.getColor(this, com.zzc.design.style.R.color.colorStatusbar));
    }

    public void fullScreen() {
        if (appCompatStatusBar == null) {
            appCompatStatusBar = new AppCompatStatusBar(this);
        }
        appCompatStatusBar.fullScreen();
        appCompatStatusBar.clearActionBarShadow();
    }

    public void initView() {

    }

    public void initData() {

    }

    protected int layoutId() {
        return 0;
    }

    public void setLoadExitAnim(boolean isLoadExitAnim) {
        this.isLoadExitAnim = isLoadExitAnim;
    }

    public void setLoadEnterAnim(boolean isLoadEnterAnim) {
        this.isLoadEnterAnim = isLoadEnterAnim;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }

    @Override
    public void finish() {
        super.finish();
        if (isLoadExitAnim) {
            super.overridePendingTransition(com.zzc.design.style.R.anim.idle, com.zzc.design.style.R.anim.slide_out_right);
        }
    }

    @TargetApi(11)
    public void startActivities(Intent[] paramArrayOfIntent) {
        super.startActivities(paramArrayOfIntent);
        anim();
    }

    @TargetApi(16)
    public void startActivities(Intent[] paramArrayOfIntent, Bundle paramBundle) {
        super.startActivities(paramArrayOfIntent, paramBundle);
        anim();
    }

    public void startActivity(Intent paramIntent) {
        super.startActivity(paramIntent);
        anim();
    }

    @TargetApi(16)
    public void startActivity(Intent paramIntent, Bundle paramBundle) {
        super.startActivity(paramIntent, paramBundle);
        anim();
    }

    public void startActivityForResult(Intent paramIntent, int paramInt) {
        super.startActivityForResult(paramIntent, paramInt);
        anim();
    }

    @TargetApi(16)
    public void startActivityForResult(Intent paramIntent, int paramInt, Bundle paramBundle) {
        super.startActivityForResult(paramIntent, paramInt, paramBundle);
        anim();
    }

    private void anim() {
        if (isLoadEnterAnim) {
            super.overridePendingTransition(com.zzc.design.style.R.anim.slide_in_left, com.zzc.design.style.R.anim.idle);
        }
    }

    @Override
    public void showLoading(String text) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("progressDialog");
        if (fragment != null && ((DialogFragment) fragment).getDialog() != null) {
            ((ProgressDialog) ((DialogFragment) fragment).getDialog()).setMessage(text);
        } else {
            AppCompatDialogFragment dialog = new ProgressDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", text);
            dialog.setArguments(args);
            if (isFinishing())
                return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (isDestroyed())
                    return;
            }
            if (getSupportFragmentManager() == null || getSupportFragmentManager().isDestroyed())
                return;
            try {
                dialog.show(getSupportFragmentManager(), "progressDialog");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hideLoading() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("progressDialog");
        if (fragment != null) {
            DialogFragment df = (DialogFragment) fragment;
            try {
                df.dismissAllowingStateLoss();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class ProgressDialogFragment extends AppCompatDialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            Bundle args = getArguments();
            if (args != null) {
                String message = args.getString("message");
                progressDialog.setMessage(message);
            }
            progressDialog.setCanceledOnTouchOutside(false);
            return progressDialog;
        }
    }

}
