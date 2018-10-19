package com.zzc.baselib.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * XSwipeRefreshLayout
 *
 * @author MUTOU
 * @date 2017/1/12
 */
public class XSwipeRefreshLayout extends SwipeRefreshLayout {

    private ViewGroup mViewGroup;

    private float mDownX;// 点击时y坐标

    private float mDownY;// 点击时y坐标

    private boolean isEnable = true;

    private boolean isPullUp;

    public XSwipeRefreshLayout(Context context) {
        super(context);
    }

    public XSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup getViewGroup() {
        return mViewGroup;
    }

    public void setViewGroup(ViewGroup viewGroup) {
        this.mViewGroup = viewGroup;
    }

    public boolean isPullUp() {
        return isPullUp;
    }

    public void setPullUp(boolean pullUp) {
        isPullUp = pullUp;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //        Log.i("TouchEvent", getClass().getSimpleName() + "-->" + "dispatchTouchEvent" + "-->" +
        //                getMotionEventName(ev) + "-->" + (super.dispatchHoverEvent(ev) ? "true" : "false"));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //        Log.i("TouchEvent", getClass().getSimpleName() + "-->" + "onInterceptTouchEvent" + "-->" +
        //                getMotionEventName(ev) + "-->" + (super.onInterceptTouchEvent(ev) ? "true" : "false"));
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                // isEnable = isEnabled(); // 记录初始的 enable 状态
                break;
            case MotionEvent.ACTION_MOVE:
                float preX = mDownX;// 移动前的x坐标
                float preY = mDownY;// 移动前的y坐标
                float nowX = ev.getX();// 时时x坐标
                float nowY = ev.getY();// 时时y坐标
                mDownX = nowX; // 重置按下的x坐标
                mDownY = nowY;// 重置按下的y坐标

                // LogUtils.d(XSwipeRefreshLayout.class.getSimpleName(), String.valueOf(Math.abs(nowX - preX) - Math.abs(nowY - preY)));
                if (Math.abs(nowX - preX) < Math.abs(nowY - preY)) {
                    //  && mViewGroup != null && mViewGroup.getMeasuredHeight() - getHeight() < 0
                    //                    return false;
                    isPullUp = nowY - preY < 0;
                } else { // 偏横向
                    if (isEnabled() && (Math.abs(nowX - preX) > 2)) {
                        super.setEnabled(false);
                    }
                    isPullUp = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isEnable != isEnabled()) {
                    super.setEnabled(isEnable); // 设置回原来的enable状态
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnable = enabled;
        super.setEnabled(enabled);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //        Log.i("TouchEvent", getClass().getSimpleName() + "-->" + "onTouchEvent" + "-->" + getMotionEventName(ev) +
        //                "-->" + (super.onTouchEvent(ev) ? "true" : "false"));
        //        if(null!= mViewGroup){
        //            if(mViewGroup.getScrollY()> 1){
        //                // 不消费
        //                return false;
        //            }else{
        //                return super.onTouchEvent(ev);
        //            }
        //        }
        return super.onTouchEvent(ev);
    }

    /*private String getMotionEventName(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            default:
                return "OTHER";
        }
    }*/
}