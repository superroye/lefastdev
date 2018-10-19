package com.zzc.baselib.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapUtil {

    public static Bitmap convertViewToBitmap(View view, int viewWidth, int viewHeight) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, viewWidth, viewHeight);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static void drawOverlay(Canvas canvas, Bitmap bitmap, float scale, int left, int top) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(newbmp, left * scale, top * scale, null);
    }

    public static boolean saveBitmap(Bitmap srcBitmap, String savePath) {
        return saveBitmap(srcBitmap, savePath, 100);
    }

    public static boolean saveBitmap(Bitmap srcBitmap, String savePath, int quality) {
        return saveBitmap(srcBitmap, CompressFormat.JPEG, savePath, quality);
    }

    public static boolean saveBitmap(Bitmap srcBitmap, CompressFormat format, String savePath, int quality) {
        if (srcBitmap != null) {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(savePath));
                srcBitmap.compress(format, quality, bos);
                bos.flush();
                bos.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    final static int TARGET_WIDTH = 720;
//	final static int TARGET_HEIGHT = 1280;

    public static int computeSampleSize(Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap rotaingImageView(float angle, Bitmap bmp) {
        Bitmap retBm = bmp;
        if (retBm != null && angle != 0f) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            retBm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        return retBm;
    }

    /**
     * @param angle
     * @param filePath
     * @Title: rotaingImageView
     * @Description: 把本地图片旋转的角度矫正，不判断内存移除情况
     * @return: void
     */
    public static void rotaingImageView(float angle, String filePath) {
        try {
            if (angle == 0) {    //无旋转直接返回
                return;
            }
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(angle);
                    Bitmap bmp = BitmapFactory.decodeFile(filePath);
                    if (bmp != null) {
                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bmp.compress(CompressFormat.JPEG, 90, bos);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
    }

    /**
     * @param file
     * @Title: getOrientation
     * @Description: 获取图片的旋转角度
     * @return: float
     */
    public static float getOrientation(String file) {
        if (file == null) {
            return 0;
        }
        float degree = 0;
        ExifInterface exif;
        try {
            exif = new ExifInterface(file);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90f;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180f;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270f;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        exif = null;
        return degree;
    }

    /**
     * @param filePath
     * @Title: isImgGif
     * @Description: 判断一个图片是不是gif图
     * @return: boolean
     */
    public static boolean isImgGif(String filePath) {
        try {
            if (filePath != null) {
                FileInputStream inputStream = new FileInputStream(filePath);
                byte[] buffer = new byte[2];
                String filecode = "";    // 文件类型代码
                // 通过读取出来的前两个字节来判断文件类型
                if (inputStream.read(buffer) != -1) {
                    for (int i = 0; i < buffer.length; i++) {
                        // 获取每个字节与0xFF进行与运算来获取高位，这个读取出来的数据不是出现负数, 并转换成字符串
                        filecode += Integer.toString((buffer[i] & 0xFF));
                    }
                    if (Integer.parseInt(filecode) == 7173) {    // 把字符串再转换成Integer进行类型判断
                        inputStream.close();
                        return true;
                    }

                }
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    public static void compressAndGenImage(String imgPath, String outPath, int maxSize) throws IOException {
        Bitmap image = getBitmap(imgPath);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 85;
        // Store the bitmap into output stream(no compress)
        if (os.toByteArray().length / 1024 > maxSize) {
            image.compress(CompressFormat.JPEG, options, os);
        } else {
            try {
                FileUtils.copyFile(new File(imgPath), new File(outPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(CompressFormat.JPEG, options, os);
        }

        // Generate compressed image file
        FileOutputStream fos = new FileOutputStream(outPath);
        fos.write(os.toByteArray());
        fos.flush();
        fos.close();
        if (image != null && !image.isRecycled()) {
            image.recycle();
        }
    }

    public static Bitmap getBitmap(String imgPath) {
        Options newOpts = new Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        if (bitmap == null)
            return bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
