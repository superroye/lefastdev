package com.zzc.baselib.support.permission;

import android.content.Context;
import android.content.DialogInterface;

import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.io.File;
import com.zzc.baselib.R;

/**
 * Created by YanZhenjie on 2018/4/29.
 */
public class InstallRationale implements Rationale<File> {

    @Override
    public void showRationale(Context context, File data, final RequestExecutor executor) {
        new PermissionDialog.Builder(context)
                .canCancel(false)
                .title(R.string.common_tips)
                .message("您的手机不允许安装应用")
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