package com.zzc.baselib.ui.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzc.baselib.R;
import com.zzc.baselib.base.AppBase;
import com.zzc.baselib.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单对话框<br>
 * <br>
 * 设置菜单选项 setMenu/setMenuText/setMenuTextId<br>
 * 获取菜单选项 {@link #getMenu()}，可以用于设置选项的颜色、背景、禁用等<br>
 * 设置菜单选项点击监听器 {@link #setOnItemClickListener(OnItemClickListener)}<br>
 * 设置是否点击选项之后隐藏菜单 {@link #setDismissOnItemClick(boolean)}<br>
 * 设置是否显示取消选项 {@link #setShowCancel(boolean)}<br>
 *
 * @author MUTOU
 */
public class MenuDialog extends BaseDialog implements View.OnClickListener {

    /**
     * 标题
     */
    private Menu mTitle;

    /**
     * 菜单选项数据
     */
    private List<Menu> mMenus;

    /**
     * 是否点击菜单选项之后隐藏菜单
     */
    private boolean mDismissOnItemClick = true;

    /**
     * 是否显示取消选项
     */
    private boolean mShowCancel = true;

    /**
     * 菜单选项点击监听器
     */
    private OnItemClickListener mListener;

    private int selectIndex = -1;

    /**
     * 从底部弹出
     */
    public MenuDialog(Context context) {
        super(context, true);
    }

    /**
     * @see {@link BaseDialog#BaseDialog(Context, boolean)}
     */
    public MenuDialog(Context context, boolean alignBottom) {
        super(context, alignBottom);
        this.mShowCancel = alignBottom;
    }

    /**
     * 获取菜单选项数据
     */
    public List<Menu> getMenu() {
        return mMenus;
    }

    /**
     * 设置菜单选项数据
     */
    public MenuDialog setMenu(List<Menu> menus) {
        mMenus = menus;
        return this;
    }

    /**
     * 设置菜单选项文字
     *
     * @param menus 菜单选项文字列表
     */
    public MenuDialog setMenuText(List<String> menus) {
        if (menus == null)
            mMenus = null;
        else {
            mMenus = new ArrayList<>();
            for (String string : menus) {
                mMenus.add(new Menu(string));
            }
        }
        return this;
    }

    /**
     * 设置菜单选项文字
     *
     * @param menus 菜单选项文字数组
     */
    public MenuDialog setMenuText(String... menus) {
        if (menus == null)
            mMenus = null;
        else {
            mMenus = new ArrayList<>();
            for (String string : menus) {
                mMenus.add(new Menu(string));
            }
        }
        return this;
    }

    /**
     * 设置菜单选项文字资源id
     *
     * @param menus 菜单选项文字资源id列表
     */
    public MenuDialog setMenuTextId(List<Integer> menus) {
        if (menus == null)
            mMenus = null;
        else {
            mMenus = new ArrayList<>();
            for (int textId : menus) {
                mMenus.add(new Menu(getContext().getString(textId)));
            }
        }
        return this;
    }

    /**
     * 设置菜单选项文字资源id
     *
     * @param menus 菜单选项文字资源id数组
     */
    public MenuDialog setMenuTextId(int... menus) {
        if (menus == null)
            mMenus = null;
        else {
            this.mMenus = new ArrayList<>();
            for (int textId : menus) {
                mMenus.add(new Menu(getContext().getString(textId)));
            }
        }
        return this;
    }

    /**
     * 设置菜单选项数据
     */
    public MenuDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitle == null) {
                mTitle = new Menu(title, ContextCompat.getColor(AppBase.app, com.zzc.design.style.R.color.textSecondary), 16, false);
            } else {
                mTitle.text = title;
            }
        }
        return this;
    }

    /**
     * 设置菜单选项数据
     */
    public MenuDialog setTitle(Menu title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置是否显示取消选项，底部弹出的话默认true
     * <p>
     * 中间显示强制为false
     *
     * @param showCancel
     */
    public MenuDialog setShowCancel(boolean showCancel) {
        mShowCancel = alignBottom && showCancel;
        return this;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public MenuDialog setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        return this;
    }

    /**
     * 设置是否点击菜单选项之后隐藏菜单，默认true
     *
     * @param dismissOnItemClick
     */
    public MenuDialog setDismissOnItemClick(boolean dismissOnItemClick) {
        mDismissOnItemClick = dismissOnItemClick;
        return this;
    }

    /**
     * 设置菜单选项点击监听器
     *
     * @param listener
     */
    public MenuDialog setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    protected View getView(ViewGroup parent) {
        if (mMenus == null || mMenus.size() == 0)
            throw new RuntimeException("Set menu data first!");
        LinearLayout layout = (LinearLayout) View.inflate(getContext(), R.layout.common_dialog_menu, null);

        // 标题
        boolean hasTitle = mTitle != null && !TextUtils.isEmpty(mTitle.text);
        if (hasTitle) {
            View titleView = mTitle.getView(getContext());
            titleView.getBackground().setLevel(alignBottom ? 0 : 1);
            layout.addView(mTitle.getView(getContext()), 0);
        }

        // 选项
        for (int i = 0; i < mMenus.size(); i++) {
            Menu menu = mMenus.get(i);
            View view = menu.getView(getContext());
            view.setSelected(i == selectIndex);
            view.setTag(i);
            view.setOnClickListener(this);
            int viewIndex = hasTitle ? i + 1 : i;
            if (!hasTitle && mMenus.size() == 1) {
                view.getBackground().setLevel(3);
            } else {
                view.getBackground().setLevel(alignBottom ? 0 : viewIndex == 0 ? 1 : i == mMenus.size() - 1 ? 2 : 0);
            }
            layout.addView(view, viewIndex);
        }

        // 取消菜单
        View cancel = layout.findViewById(R.id.menu_cancel_button);
        if (mShowCancel) {
            cancel.setVisibility(View.VISIBLE);
            cancel.setTag(-1);
            cancel.setOnClickListener(this);
            cancel.getBackground().setLevel(alignBottom ? 0 : 2);
        } else {
            cancel.setVisibility(View.GONE);
        }
        return layout;
    }

    @Override
    public void onClick(View v) {
        int index = Integer.parseInt(v.getTag().toString());
        if (index < 0) { // 取消
            cancel();
            if (mListener != null && mListener instanceof OnItemClickListener2) {
                ((OnItemClickListener2) mListener).onCancel();
            }
            return;
        }
        Menu menu = mMenus.get(index);
        if (!menu.enabled) {
            return;
        }
        if (menu.selectable) {
            if (selectIndex >= 0 && selectIndex < mMenus.size()) {
                mMenus.get(selectIndex).getView(getContext()).setSelected(false);
            }
            menu.getView(getContext()).setSelected(true);
            selectIndex = index;
        }

        if (mDismissOnItemClick) {
            dismiss();
        }
        if (mListener != null) {
            mListener.onItemClick(this, index);
        }
    }

    /**
     * 菜单选项数据模型
     */
    public static class Menu {

        /**
         * 选项view，如果设置了，则会使用此view
         */
        public View view;

        /**
         * 选项文字
         */
        public String text = "";

        /**
         * 选项文字颜色
         */
        public int textColor = 0;

        /**
         * 选项字体大小，默认单位sp
         */
        public float textSize = 0;

        /**
         * 选项文字大小的单位，默认sp
         */
        public int textSizeUnit = TypedValue.COMPLEX_UNIT_SP;

        /**
         * 选项是否启用
         */
        public boolean enabled = true;

        /**
         * 选项是否可选
         */
        public boolean selectable = true;

        public Menu() {
        }

        public Menu(View view) {
            this.view = view;
        }

        public Menu(String text) {
            this.text = text;
        }

        public Menu(String text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }

        public Menu(String text, @ColorInt int textColor, float textSize, boolean enabled) {
            this.text = text;
            this.textColor = textColor;
            this.textSize = textSize;
            this.enabled = enabled;
        }

        public void setSelectable(boolean selectable) {
            this.selectable = selectable;
        }

        /**
         * 获取选项view
         */
        public View getView(Context context) {
            if (view == null) {
                TextView button = new TextView(new ContextThemeWrapper(context, R.style.ButtonMenu_Selector)); // 带样式的context
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dp2px(50));
                lp.bottomMargin = 1;
                button.setLayoutParams(lp);
                button.setText(text);
                if (textSize > 0) {
                    button.setTextSize(textSizeUnit, textSize);
                }
                if (textColor != 0) {
                    button.setTextColor(textColor);
                }
                if (!enabled) {
                    button.setEnabled(false);
                }
                view = button;
            }
            return view;
        }

    }

    /**
     * 菜单选项点击监听器
     */
    public static interface OnItemClickListener {

        public void onItemClick(MenuDialog dialog, int index);
    }

    public static interface OnItemClickListener2 extends OnItemClickListener {

        public void onCancel();
    }

}