package com.zzc.baselib.ui.viewtools;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Roye on 2018/6/6.
 */
public class CanClickChecker {

    private View clickView;
    private int clickValues;
    private int canClickValue;

    public CanClickChecker(View clickView) {
        this.clickView = clickView;
    }

    public void setCheckWidgets(View... widgets) {
        clickValues = 0;
        for (int i = 0; i < widgets.length; i++) {
            final int c = i;
            canClickValue |= 1 << c;
            if (widgets[i] instanceof TextView) {
                ((TextView) widgets[c]).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int bitValue = 1 << c;
                        if (s.length() == 0) {
                            clickValues &= (0xffff ^ bitValue);
                        } else {
                            clickValues |= bitValue;
                        }
                        setClickState();
                    }
                });
            } else if (widgets[i] instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) widgets[i];
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int bitValue = 1 << c;
                        if (group.getCheckedRadioButtonId() < 1) {
                            clickValues &= (0xffff ^ bitValue);
                        } else {
                            clickValues |= bitValue;
                        }
                        setClickState();
                    }
                });
            }
        }
        setClickState();
    }


    private void setClickState() {
        clickView.setEnabled(canClickValue == clickValues);
    }
}
