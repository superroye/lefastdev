package com.zzc.baselib.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zzc.baselib.R;

public abstract class ToolbarBaseActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    protected Toolbar toolbar;
    protected TextView customTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar  = findView(R.id.toolbar);
        customTitleName  = findView(R.id.custom_title_name);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            if (customTitleName != null) {
                toolbar.setTitle("");
                customTitleName.setText(getTitle());
            } else {
                toolbar.setTitle(getTitle());
            }
            if (hideNavigationIcon()) {
                toolbar.setNavigationIcon(null);
            } else {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
            int menu = getToolbarInflateMenu();
            if (menu > 0) {
                toolbar.getMenu().clear();
                toolbar.inflateMenu(menu);
                toolbar.setOnMenuItemClickListener(this);
            }
        }
    }

    protected int getToolbarInflateMenu() {
        return 0;
    }

    protected boolean hideNavigationIcon() {
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    public void setTitleName(CharSequence title) {
        if (customTitleName != null) {
            customTitleName.setText(title);
        } else if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
