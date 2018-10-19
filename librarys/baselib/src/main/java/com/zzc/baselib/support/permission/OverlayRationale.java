package com.zzc.baselib.support.permission;

import android.content.Context;
import android.content.DialogInterface;

import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.zzc.baselib.R;

/**
 * Created by YanZhenjie on 2018/5/30.
 */
public class OverlayRationale implements Rationale<Void> {
    @Override
    public void showRationale(Context context, Void data, final RequestExecutor executor) {
        String message = String.format(context.getString(R.string.common_permission_resume_message), context.getString(R.string.common_floatwindow));
        new PermissionDialog.Builder(context)
                .canCancel(false)
                .title(R.string.common_tips)
                .message(message)
                .positive(R.string.common_resume)
                .cancel(R.string.common_cancel)
                .onClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (DialogInterface.BUTTON_POSITIVE == which) {
                            executor.execute();
                        } else {
                            executor.cancel();
                        }
                    }
                }).createDialog(true).show();
    }
}