package com.zzc.baselib.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zzc.baselib.R;

/**
 * 自定义dialog基础类
 *
 * @author MUTOU
 */
public abstract class BaseDialog extends Dialog {

    /** 是否位于底部 */
    protected boolean alignBottom = false;

    public BaseDialog(Context context) {
        this(context, false);
    }

    /**
     * @param context
     * @param alignBottom 是否位于底部，从底部弹出
     */
    public BaseDialog(Context context, boolean alignBottom) {
        this(context, R.style.Dialog, alignBottom);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        this(context, themeResId, false);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId, boolean alignBottom) {
        super(context, themeResId);
        this.alignBottom = alignBottom;
        if (alignBottom) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
            onWindowAttributesChanged(layoutParams);
            getWindow().setWindowAnimations(R.style.DialogAnim_Bottom);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = new FrameLayout(getContext());
        View view = getView(layout);
        view.setMinimumWidth(getMinimumWidth());
        layout.addView(view);
        setContentView(layout);
    }

    /**
     * dialog 最小宽度
     *
     * @return
     */
    protected int getMinimumWidth() {
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        return alignBottom ? width : (int) (width * 0.75);
    }

    /**
     * 获取Dialog视图,由子类实现并返回
     *
     * @return
     */
    protected abstract View getView(ViewGroup parent);

}
