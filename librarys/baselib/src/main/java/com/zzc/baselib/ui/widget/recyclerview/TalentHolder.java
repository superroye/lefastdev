package com.zzc.baselib.ui.widget.recyclerview;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zzc.baselib.arch.ActivityUtils;

import java.lang.reflect.Field;

/**
 * Created by Roye on 2016/12/10.
 */

public abstract class TalentHolder<T> extends RecyclerView.ViewHolder {

    String TAG = TalentHolder.class.getSimpleName();

    public T itemValue;
    public OnRecyclerviewItemClickListener mItemClickListener;

    public TalentHolder(View itemView) {
        super(itemView);
        initView();
    }

    public final void bind(T data) {
        itemValue = data;
        Activity activity = ActivityUtils.getActivity(itemView.getContext());
        if (activity != null && (activity.isFinishing() || activity.isDestroyed())) {
            Log.w(TAG, "activity is finishing or destroyed.");
            return;
        }
        toView();
    }

    public void initView() {
        Field[] fields = getClass().getDeclaredFields();
        if (fields != null) {
            Resources res = itemView.getResources();
            String packageName = itemView.getContext().getPackageName();
            try {
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getAnnotation(AutoView.class) != null) {
                        String viewName = fields[i].getName();
                        View view = itemView.findViewById(res.getIdentifier(viewName, "id", packageName));
                        if (view != null) {
                            fields[i].setAccessible(true);
                            fields[i].set(this, view);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setOnItemClick(OnRecyclerviewItemClickListener listener) {
        mItemClickListener = listener;

        if (listener == null || itemView.hasOnClickListeners()) {//不覆盖item的原来click事件
            return;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TalentHolderInfo holderInfo = new TalentHolderInfo(TalentHolder.this.getClass(), itemValue, getAdapterPosition());
                mItemClickListener.onItemClickListener(v, holderInfo);
            }
        });
    }

    //局部刷新
    public void onPayload(Object payload) {

    }

    public <T extends View> T findV(int resId) {
        return (T) itemView.findViewById(resId);
    }

    public abstract void toView();

    public void recycle() {
    }
}
