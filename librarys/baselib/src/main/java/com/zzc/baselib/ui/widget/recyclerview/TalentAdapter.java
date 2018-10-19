package com.zzc.baselib.ui.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roye on 2016/12/10.
 */

public class TalentAdapter extends RecyclerView.Adapter {

    String TAG = TalentAdapter.class.getSimpleName();

    List mItems = new ArrayList<>();

    List<Class> providers = new ArrayList<>();
    List<Class> clss = new ArrayList<>();
    LayoutInflater inflater;
    OnRecyclerviewItemClickListener onItemClickListener;

    public TalentAdapter() {
        this(null);
    }

    public TalentAdapter(List items) {
        if (items != null) {
            mItems = items;
        }
    }

    public List getItems() {
        return mItems;
    }

    public void resetItems(List items) {
        resetItems(items, true);
    }

    public void resetItems(List items, boolean notify) {
        if (items != null) {
            mItems = items;
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }

    public void removeItem(int position) {
        if (mItems != null && mItems.size() > position) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public void setOnRecyclerviewItemClickListener(OnRecyclerviewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        Class entityClass = providers.get(viewType);
        Object holder = null;
        try {
            HolderRes holderRes = (HolderRes) entityClass.getAnnotation(HolderRes.class);
            View root = LayoutInflater.from(parent.getContext()).inflate(holderRes.value(), parent, false);
            holder = entityClass.getConstructor(View.class).newInstance(root);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (TalentHolder) holder;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = mItems.get(position);
        int index = clss.indexOf(obj.getClass());
        if (index > -1) {
            return index;
        } else {
            throw new RuntimeException("has not supported Holder type.");
        }
    }

    public Class getDataType(Class holderClass) {
        Class cls = null;
        try {
            cls = (Class) (((ParameterizedType) holderClass.getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return cls;
    }

    public void addHolderType(Class<? extends TalentHolder> holderClass) {
        if (providers.contains(holderClass)) {
            Log.w(TAG, "reduplicate holder type.");
        } else {
            Class cls = getDataType(holderClass);
            if (clss.contains(cls)) {
                Log.w(TAG, "reduplicate holder DataType.");
            } else {
                clss.add(cls);
                providers.add(holderClass);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TalentHolder) holder).bind(mItems.get(position));
        ((TalentHolder) holder).setOnItemClick(onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (payloads.isEmpty()) {
            ((TalentHolder) holder).bind(mItems.get(position));
            ((TalentHolder) holder).setOnItemClick(onItemClickListener);
        } else {
            ((TalentHolder) holder).onPayload(payloads.get(0));
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof TalentHolder) {
            ((TalentHolder) holder).recycle();
        }
    }
}
