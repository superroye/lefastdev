package com.zzc.baselib.support.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Options;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;
import com.zzc.baselib.R;

import java.io.File;
import java.util.List;

/**
 * @author Roye
 * @date 2018/8/2
 */
public class ZZPermission {


    private ZZPermission() {
        super();
    }

    public static Options with(Fragment fragment) {
        return AndPermission.with(fragment);
    }

    public static Options with(android.app.Fragment fragment) {
        return AndPermission.with(fragment);
    }

    public static Options with(Context context) {
        return AndPermission.with(context);
    }

    public static boolean hasAlwaysDeniedPermission(Fragment fragment, List<String> deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(android.app.Fragment fragment, List<String> deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(Context context, List<String> deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(Fragment fragment, String... deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(android.app.Fragment fragment, String... deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(Context context, String... deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions);
    }

    public static boolean hasPermissions(Fragment fragment, String... permissions) {
        return AndPermission.hasPermissions(fragment, permissions);
    }

    public static boolean hasPermissions(android.app.Fragment fragment, String... permissions) {
        return AndPermission.hasPermissions(fragment, permissions);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        return AndPermission.hasPermissions(context, permissions);
    }

    public static boolean hasPermissions(Fragment fragment, String[]... permissions) {
        return AndPermission.hasPermissions(fragment, permissions);
    }

    public static boolean hasPermissions(android.app.Fragment fragment, String[]... permissions) {
        return AndPermission.hasPermissions(fragment, permissions);
    }

    public static boolean hasPermissions(Context context, String[]... permissions) {
        return AndPermission.hasPermissions(context, permissions);
    }

    public static Uri getFileUri(Fragment fragment, File file) {
        return AndPermission.getFileUri(fragment, file);
    }

    public static Uri getFileUri(android.app.Fragment fragment, File file) {
        return AndPermission.getFileUri(fragment, file);
    }

    public static Uri getFileUri(Context context, File file) {
        return AndPermission.getFileUri(context, file);
    }

    /**
     * 获取app的录音权限是否打开
     * android 6.0以下兼容
     */
    public static boolean hasAudioRecordPermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return hasPermissions(context, Manifest.permission.RECORD_AUDIO);
        }
        // 音频获取源
        int audioSource = MediaRecorder.AudioSource.MIC;
        // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
        int sampleRateInHz = 44100;
        // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        // 缓冲区字节大小
        int bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        return true;
    }

    public static void openPermissionSetting(final Context context, List<String> permissions, Setting.Action action) {
        if (context != null && context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        final Setting.Action action1 = action != null ? action : new Setting.Action() {
            @Override
            public void onAction() {

            }
        };
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
                            AndPermission.with(context)
                                    .runtime()
                                    .setting()
                                    .onComeback(action1)
                                    .start();
                        } else {
                            action1.onAction();
                        }
                    }
                }).createDialog(true).show();
    }

    public static void tryOpenPermissionSetting(Context context, List<String> permissions) {
        if (hasAlwaysDeniedPermission(context, permissions)) {
            openPermissionSetting(context, permissions, null);
        }
    }

}
