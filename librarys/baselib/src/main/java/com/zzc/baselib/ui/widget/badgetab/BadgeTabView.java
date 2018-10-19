package com.zzc.baselib.ui.widget.badgetab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzc.baselib.R;

/**
 * 自定义Tab控件<br/>
 * <p>
 * 思路:<br/>
 * {@link android.widget.RadioGroup} + {@link android.widget.RadioButton}<br/>
 * <p>
 * 功能简介:<br/>
 * 1. 简单的图标+文本(上下排列);<br/>
 * 2. 支持小圆点或者带文本, 如: 未读消息数;<br/>
 * 3. 可单独使用, 比如作为{@link android.support.design.widget.TabLayout.Tab#setCustomView(View)}添加自定义View;<br/>
 * 4. 可结合{@link BadgeTabLayout}使用, 比如{@link android.widget.RadioGroup} + {@link android.widget.RadioButton}<br/>
 *
 * @author MUTOU
 * @date 2018/3/30 16:21
 */
public class BadgeTabView extends RelativeLayout {

    private boolean mBroadcasting;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    /** 图标 */
    private ImageView mIconView;

    /** 文本 */
    private TextView mTextView;

    /** 圆点/文本 */
    private TextView mBadgeView;

    /**
     * Badge 的背景
     *
     * @see #mBadgeView
     */
    private Drawable mBadgeBackground;

    /**
     * Badge 的最小宽高
     *
     * @see #mBadgeView
     */
    private int mBadgeMinWH;

    /**
     * Badge 的最小宽高, 有text的话, 默认等于mBadgeMinWH
     *
     * @see #mBadgeView
     * @see #mBadgeMinWH
     */
    private int mBadgeMinWH_withText;

    /**
     * Badge 上显示的文本颜色
     *
     * @see #mBadgeView
     */
    private ColorStateList mBadgeTextColor;

    /**
     * Badge 的文本大小
     *
     * @see #mBadgeView
     */
    private int mBadgeTextSize;

    /**
     * mBadgeView padding
     *
     * @see #mBadgeView
     */
    private Rect mBadgeMarginRect = new Rect();

    public BadgeTabView(Context context) {
        this(context, null);
    }

    public BadgeTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 可点击
        setClickable(true);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BadgeTabView, defStyle, 0);
        // 初始化 icon
        Drawable src = ta.getDrawable(R.styleable.BadgeTabView_src);
        int srcMargin = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_margin, -1);
        int srcMarginLeft, srcMarginTop, srcMarginRight, srcMarginBottom;
        if (srcMargin >= 0) {
            srcMarginLeft = srcMarginTop = srcMarginRight = srcMarginBottom = srcMargin;
        } else {
            srcMarginLeft = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_marginLeft, 0);
            srcMarginTop = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_marginTop, 0);
            srcMarginRight = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_marginRight, 0);
            srcMarginBottom = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_marginBottom, 0);
        }
        LayoutParams iconParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iconParams.addRule(CENTER_HORIZONTAL);
        iconParams.setMargins(srcMarginLeft, srcMarginTop, srcMarginRight, srcMarginBottom);
        mIconView = new ImageView(context);
        mIconView.setLayoutParams(iconParams);
        mIconView.setId(R.id.BadgeTabLayout_icon);
        if (src != null) {
            mIconView.setImageDrawable(src);
        }
        addView(mIconView, iconParams);

        // 初始化 text
        CharSequence text = ta.getText(R.styleable.BadgeTabView_text);
        ColorStateList textColor = ta.getColorStateList(R.styleable.BadgeTabView_text_color);
        int textSize = ta.getDimensionPixelSize(R.styleable.BadgeTabView_text_size, 0);
        int textMargin = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_margin, -1);
        int textMarginLeft, textMarginTop, textMarginRight, textMarginBottom;
        if (textMargin >= 0) {
            textMarginLeft = textMarginTop = textMarginRight = textMarginBottom = textMargin;
        } else {
            textMarginLeft = ta.getDimensionPixelSize(R.styleable.BadgeTabView_text_marginLeft, 0);
            textMarginTop = ta.getDimensionPixelSize(R.styleable.BadgeTabView_text_marginTop, 0);
            textMarginRight = ta.getDimensionPixelSize(R.styleable.BadgeTabView_text_marginRight, 0);
            textMarginBottom = ta.getDimensionPixelSize(R.styleable.BadgeTabView_text_marginBottom, 0);
        }
        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParams.addRule(CENTER_HORIZONTAL);
        textParams.addRule(BELOW, R.id.BadgeTabLayout_icon);
        textParams.setMargins(textMarginLeft, textMarginTop, textMarginRight, textMarginBottom);
        mTextView = new TextView(context);
        mTextView.setLayoutParams(textParams);
        mTextView.setId(R.id.BadgeTabLayout_text);
        mTextView.setMaxLines(1);
        if (textColor != null) {
            mTextView.setTextColor(textColor);
        }
        if (textSize > 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (!TextUtils.isEmpty(text)) {
            mTextView.setText(text);
        }
        addView(mTextView, textParams);
        // 初始化选中状态
        boolean checked = ta.getBoolean(R.styleable.BadgeTabView_checked, false);
        setChecked(checked);

        // 初始化 badge, 如果有的话
        boolean badgeVisible = ta.getBoolean(R.styleable.BadgeTabView_badge_visible, false);
        mBadgeBackground = ta.getDrawable(R.styleable.BadgeTabView_badge_background);
        mBadgeMinWH = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_minWH, (int) (getResources().getDisplayMetrics().density * 5));
        mBadgeMinWH_withText = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_minWH_withText, mBadgeMinWH);
        // badge text
        mBadgeTextColor = ta.getColorStateList(R.styleable.BadgeTabView_badge_textColor);
        mBadgeTextSize = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_textSize, 0);
        // badge margin
        int badgeMargin = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_margin, -1);
        if (badgeMargin >= 0) {
            mBadgeMarginRect.set(badgeMargin, badgeMargin, badgeMargin, badgeMargin);
        } else {
            mBadgeMarginRect.left = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginLeft, 0);
            mBadgeMarginRect.top = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginTop, 0);
            mBadgeMarginRect.right = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginRight, 0);
            mBadgeMarginRect.bottom = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginBottom, 0);
        }

        if (badgeVisible) {
            mBadgeView = addBadgeView();
            CharSequence badgeText = ta.getText(R.styleable.BadgeTabView_badge_text);
            setBadgeView(badgeText, true);
        }

        ta.recycle();
    }

    /** 如果没选中的话, 点击选中 */
    @Override
    public boolean performClick() {
        if (!isChecked()) {
            setChecked(true);
        }

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    /** 是否选中状态, 实际上是读取{@link #isSelected()} */
    public boolean isChecked() {
        return isSelected();
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (isChecked() != checked) {
            setSelected(checked);
            mIconView.setSelected(checked);
            mTextView.setSelected(checked);
            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }
            mBroadcasting = true;

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
            }
            mBroadcasting = false;
        }
    }

    /**
     * 隐藏 Badge
     *
     * @see #getBadgeView
     */
    public void hideBadge() {
        setBadge(false, null);
    }

    /**
     * 设置 Badge: 小圆点
     *
     * @see #getBadgeView
     */
    public void showBadge() {
        setBadge(true, null);
    }

    /**
     * 设置 Badge: 数字
     *
     * @see #getBadgeView
     */
    public void showBadge(int number) {
        showBadge(String.valueOf(number));
    }

    /**
     * 设置 Badge: 文本
     *
     * @see #getBadgeView
     */
    public void showBadge(CharSequence text) {
        setBadge(true, text);
    }

    /**
     * 设置小圆点/文本
     *
     * @param visible 是否可见
     * @param text 空?小圆点: 文本
     */
    private void setBadge(boolean visible, CharSequence text) {
        if (visible) {
            if (mBadgeView == null) {
                mBadgeView = addBadgeView();
                setBadgeView(text, true);
            } else {
                setBadgeView(text, false);
            }
            mBadgeView.setVisibility(View.VISIBLE);
        } else if (mBadgeView != null) {
            mBadgeView.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the text appearance from the specified style resource.
     * <p>
     *
     * @param resId the resource identifier of the style to apply
     *
     * @attr ref android.R.styleable#TextView_textAppearance
     */
    public void setBadgeAppearance(int resId) {
        final TypedArray ta = getContext().obtainStyledAttributes(resId, R.styleable.BadgeTabView);

        // 初始化 badge, 如果有的话
        mBadgeBackground = ta.getDrawable(R.styleable.BadgeTabView_badge_background);
        mBadgeMinWH = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_minWH, (int) (getResources().getDisplayMetrics().density * 5));
        // badge text
        mBadgeTextColor = ta.getColorStateList(R.styleable.BadgeTabView_badge_textColor);
        mBadgeTextSize = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_textSize, 0);
        // badge margin
        int badgeMargin = ta.getDimensionPixelSize(R.styleable.BadgeTabView_src_margin, -1);
        if (badgeMargin >= 0) {
            mBadgeMarginRect.set(badgeMargin, badgeMargin, badgeMargin, badgeMargin);
        } else {
            mBadgeMarginRect.left = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginLeft, 0);
            mBadgeMarginRect.top = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginTop, 0);
            mBadgeMarginRect.right = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginRight, 0);
            mBadgeMarginRect.bottom = ta.getDimensionPixelSize(R.styleable.BadgeTabView_badge_marginBottom, 0);
        }

        if (mBadgeView != null) {
            if (mBadgeTextColor != null) {
                mBadgeView.setTextColor(mBadgeTextColor);
            }
            if (mBadgeBackground != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBadgeView.setBackground(mBadgeBackground);
                } else {
                    mBadgeView.setBackgroundDrawable(mBadgeBackground);
                }
            }
            LayoutParams layoutParams = (LayoutParams) mBadgeView.getLayoutParams();
            layoutParams.setMargins(mBadgeMarginRect.left, mBadgeMarginRect.top, mBadgeMarginRect.right, mBadgeMarginRect.bottom);
        }

        ta.recycle();
    }

    /**
     * Badge 是否可见
     */
    public boolean isBadgeVisible() {
        return mBadgeView != null && mBadgeView.getVisibility() == View.VISIBLE;
    }

    /** tab内的Badge文本控件 */
    public TextView getBadgeView() {
        return mBadgeView;
    }

    /** tab内的文本控件 */
    public TextView getTextView() {
        return mTextView;
    }

    /** tab内的图标控件 */
    public ImageView getIconView() {
        return mIconView;
    }

    private TextView addBadgeView() {
        TextView badgeView = new TextView(getContext());
        badgeView.setMaxLines(1);
        badgeView.setGravity(Gravity.CENTER);
        if (mBadgeTextColor != null) {
            badgeView.setTextColor(mBadgeTextColor);
        }
        badgeView.setId(R.id.BadgeTabLayout_badge);
        if (mBadgeBackground != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                badgeView.setBackground(mBadgeBackground);
            } else {
                badgeView.setBackgroundDrawable(mBadgeBackground);
            }
        }
        LayoutParams badgeParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        badgeParams.addRule(RIGHT_OF, R.id.BadgeTabLayout_icon);
        badgeParams.addRule(ALIGN_TOP, R.id.BadgeTabLayout_icon);
        badgeParams.setMargins(mBadgeMarginRect.left, mBadgeMarginRect.top, mBadgeMarginRect.right, mBadgeMarginRect.bottom);
        badgeView.setLayoutParams(badgeParams);
        addView(badgeView, badgeParams);
        return badgeView;
    }

    private void setBadgeView(CharSequence badgeText, boolean init) {
        if (mBadgeView == null) {
            return;
        }
        boolean emptyPre = TextUtils.isEmpty(mBadgeView.getText());
        boolean emptyNow = TextUtils.isEmpty(badgeText);
        if (init || (emptyPre && !emptyNow) || (!emptyPre && emptyNow)) {
            // 文本内容: 从无到有 || 从有到无
            if (emptyNow) {
                mBadgeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);
                if (mBadgeView.getPaddingLeft() > 0 || mBadgeView.getPaddingRight() > 0
                        || mBadgeView.getPaddingTop() > 0 || mBadgeView.getPaddingBottom() > 0) {
                    mBadgeView.setPadding(0, 0, 0, 0);
                }
                mBadgeView.setMinWidth(mBadgeMinWH);
                mBadgeView.setMinHeight(mBadgeMinWH);
            } else {
                mBadgeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBadgeTextSize);
                // 校验PaddingLeft & PaddingRight
                if (mBadgeView.getPaddingLeft() <= 0 || mBadgeView.getPaddingRight() <= 0) {
                    Rect paddingRect = new Rect();
                    // 看下Background有没有设置Padding
                    boolean haspPadding = mBadgeView.getBackground().getPadding(paddingRect);
                    if (!haspPadding || paddingRect.left <= 0 || paddingRect.right <= 0) {
                        // Background也没有设置PaddingLeft & PaddingRight
                        int padding = (int) (getResources().getDisplayMetrics().density * 3);
                        paddingRect.left = paddingRect.right = padding;
                        mBadgeView.setPadding(paddingRect.left, paddingRect.top, paddingRect.right, paddingRect.bottom);
                    }
                }
                mBadgeView.setMinWidth(mBadgeMinWH_withText);
                mBadgeView.setMinHeight(mBadgeMinWH_withText);
            }
        }
        mBadgeView.setText(badgeText);
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     *
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(BadgeTabView buttonView, boolean isChecked);
    }
}
