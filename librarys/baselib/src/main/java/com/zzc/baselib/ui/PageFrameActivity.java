package com.zzc.baselib.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zzc.baselib.base.BaseApplication;
import com.zzc.baselib.R;

/**
 * Created by Roye on 2016/1/19.
 */
public class PageFrameActivity extends BaseActivity {

    public final static String EXTRA_CLASS = "fragmentClassName";
    public final static String EXTRA_TITLE = "fragmentTitle";

    public static void launchActivity(Context context, Class<? extends Fragment> cls) {
        launchActivity(context, cls, null, null);
    }

    public static void launchActivity(Context context, Class<? extends Fragment> cls, Bundle extras) {
        launchActivity(context, cls, null, extras);
    }

    public static void launchActivity(Context context, Class<? extends Fragment> cls, String title, Bundle extras) {
        Intent intent = new Intent(context, PageFrameActivity.class);
        intent.putExtra(EXTRA_CLASS, cls.getName());
        intent.putExtra(EXTRA_TITLE, title);
        if (context == BaseApplication.app) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (extras != null)
            intent.putExtras(extras);
        context.startActivity(intent);
    }

    public static Intent getIntent(Context context, Class<? extends Fragment> cls) {
        Intent intent = new Intent(context, PageFrameActivity.class);
        intent.putExtra(EXTRA_CLASS, cls.getName());

        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager() == null)
            return;
        Fragment f = getSupportFragmentManager().findFragmentByTag("root");
        if (f == null)
            return;
        f.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screen = getIntent().getIntExtra("screen", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (screen == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (screen == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


        Intent data = getIntent();
        if (data != null) {
            String fragmentClassName = data.getStringExtra(EXTRA_CLASS);
            String fragmentTitle = data.getStringExtra(EXTRA_TITLE);
            if (!TextUtils.isEmpty(fragmentTitle)) {
                setContentView(R.layout.base_activity_page_frame_inc_title);
                TextView titleView = findView(R.id.custom_title_name);
                titleView.setText(fragmentTitle);
                Toolbar toolbar = findView(R.id.toolbar);
                toolbar.setTitle("");
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            } else {
                setContentView(R.layout.base_activity_page_frame);
            }

            Fragment fragment = null;
            try {
                fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fragment != null) {
                Bundle args = new Bundle();
                if (getIntent() != null && getIntent().getExtras() != null) {
                    args.putAll(getIntent().getExtras());
                }
                fragment.setArguments(args);
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_content, fragment, "root")
                        .commitAllowingStateLoss();
            }
        }


    }

}
