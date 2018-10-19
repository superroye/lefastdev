package com.zzc.baselib.ui.widget.statusbar;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.zzc.baselib.arch.ActivityUtils;

/**
 * @author Roye
 * @date 2018/8/16
 */
public class StatusBarRecyclerView extends RecyclerView implements LifecycleObserver {

    int scrolly;
    AppCompatStatusBar appCompatStatusBar;

    public StatusBarRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Context lifecyContext = ActivityUtils.getActivity(context);
        if (lifecyContext instanceof LifecycleOwner) {
            ((LifecycleOwner) lifecyContext).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        appCompatStatusBar = new AppCompatStatusBar(getContext());

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrolly += dy;
                appCompatStatusBar.onScrollY(scrolly);
            }
        });
    }

    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            appCompatStatusBar.onScrollY(scrolly);
        } else {
            appCompatStatusBar.resetState();
        }
    }


}
