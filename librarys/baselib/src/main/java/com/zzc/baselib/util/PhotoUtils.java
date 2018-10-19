package com.zzc.baselib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liqi on 2016-12-19.
 */

public class PhotoUtils {

    public static String cameraPath;

    public static void openAblum(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(Uri
                .parse("content://media/internal/images/media"));
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);
            }
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
        }
        Intent chooser = Intent.createChooser(intent, "选择照片");
        if (chooser.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(chooser, requestCode);
        } else {
            ToastUtils.showToast("你的系统不支持该功能");
        }
    }

    public static void openAblumDirs(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent,1);
    }

    public static void openCamera(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile(activity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cameraPath = imageFile.getAbsolutePath();
            if (!TextUtils.isEmpty(cameraPath)) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                takePictureIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivityForResult(takePictureIntent, requestCode);
            } else {
                ToastUtils.showToast("创建文件失败，拍照失败");
            }
        }
    }

    public static void sendBroadcast(Activity activity) {
        if (!TextUtils.isEmpty(cameraPath))
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(cameraPath))));
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir("temp");
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFile.setWritable(true, false);
        return imageFile;
    }
}
