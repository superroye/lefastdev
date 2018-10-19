package com.zzc.baselib.support.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.zzc.baselib.R;

import java.util.List;

/**
 * Created by YanZhenjie on 2018/1/1.
 */
public final class RuntimeRationale implements Rationale<List<String>> {

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String title = context.getString(R.string.common_title_tips, TextUtils.join("\n", permissionNames));
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new PermissionDialog.Builder(context)
                .canCancel(false)
                .title(title)
                .message(message)
                .positive(R.string.common_setting)
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