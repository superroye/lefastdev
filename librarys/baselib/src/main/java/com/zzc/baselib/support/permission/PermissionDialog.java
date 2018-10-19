package com.zzc.baselib.support.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zzc.baselib.R;

/**
 * Created by elileo on 2018/6/5.
 */
public class PermissionDialog extends Dialog {

    private TextView mTitle;
    private TextView mMessage;
    private TextView mButtonPositive;
    private TextView mButtonCancel;

    private OnClickListener mListener;

    private PermissionDialog(Context context) {
        this(context, 0, true);
    }

    private PermissionDialog(Context context, int theme, boolean isMsgCenter) {
        super(context, theme);
        setContentView(R.layout.common_permission_alert);
        initView(isMsgCenter);
    }

    private void initView(boolean isMsgCenter) {
        mTitle = findViewById(R.id.dialog_title_tv);
        mTitle.setVisibility(View.GONE);
        mMessage = findViewById(R.id.dialog_message_tv);
        mButtonPositive = findViewById(R.id.dialog_sure_tv);
        mButtonCancel = findViewById(R.id.dialog_cancel_tv);
        mMessage.setGravity(isMsgCenter ? Gravity.CENTER_HORIZONTAL : Gravity.NO_GRAVITY);
        mMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        mButtonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClick(PermissionDialog.this, BUTTON_POSITIVE);
                }
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClick(PermissionDialog.this, BUTTON_NEGATIVE);
                }
            }
        });
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public void setMessage(CharSequence message) {
        mMessage.setText(message);
    }

    public void setPositive(CharSequence positive) {
        mButtonPositive.setText(positive);
    }

    public void setOnClickListener(final OnClickListener listener) {
        mListener = listener;
    }

    public void setCancel(CharSequence cancel) {
        mButtonCancel.setText(cancel);
    }

    public void setCancelColor(int color) {
        mButtonCancel.setTextColor(color);
    }

    public static class Builder {
        private Context mContext;
        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence mPositive;
        private CharSequence mCancel;
        private OnClickListener mListener;
        private boolean mCanCancel;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder title(int resId) {
            title(mContext.getString(resId));
            return this;
        }

        public Builder title(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder message(int resId) {
            message(mContext.getString(resId));
            return this;
        }

        public Builder message(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder positive(int resId) {
            positive(mContext.getString(resId));
            return this;
        }

        public Builder positive(CharSequence positive) {
            mPositive = positive;
            return this;
        }

        public Builder cancel(int resId) {
            cancel(mContext.getString(resId));
            return this;
        }

        public Builder cancel(CharSequence cancle) {
            mCancel = cancle;
            return this;
        }

        public Builder canCancel(boolean canCancel) {
            mCanCancel = canCancel;
            return this;
        }


        public Builder onClickListener(OnClickListener listener) {
            mListener = listener;
            return this;
        }

        public PermissionDialog createDialog(boolean isMsgCenter) {
            PermissionDialog alert = new PermissionDialog(mContext, R.style.Dialog_Transparent, isMsgCenter);

            if (mTitle == null) {
                alert.mTitle.setVisibility(View.GONE);
            } else {
                alert.mTitle.setVisibility(View.VISIBLE);
                alert.setTitle(mTitle);
            }

            if (mMessage == null) {
                alert.mMessage.setVisibility(View.GONE);
            } else {
                alert.setMessage(mMessage);
            }
            if (mPositive == null) {
                alert.mButtonPositive.setVisibility(View.GONE);
            } else {
                alert.setPositive(mPositive);
            }

            if (mCancel == null) {
                alert.mButtonCancel.setVisibility(View.GONE);
            } else {
                alert.setCancel(mCancel);
            }

            alert.setCanceledOnTouchOutside(mCanCancel);
            alert.setCancelable(mCanCancel);
            alert.setOnClickListener(mListener);

            if (!(mContext instanceof Activity)) {
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            return alert;
        }
    }
}
