package com.zzc.baselib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzc.baselib.R;

/**
 * Created by Roye on 2018/5/25.
 */
public class BaseToolbarFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        if (toolbar != null)
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });

        return root;
    }

    public void setTitle(String title) {
        TextView customTitleName = findView(R.id.custom_title_name);
        if (customTitleName != null)
            customTitleName.setText(title);
    }
}
