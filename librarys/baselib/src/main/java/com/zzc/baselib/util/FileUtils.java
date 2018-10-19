package com.zzc.baselib.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static File getFileDir(Context context) {
        return getFileDir(context, "");
    }

    public static File getFileDir(Context context, String type) {
        return getFileDir(context, type, false);
    }

    public static File getFileDir(Context context, String type, boolean sdcard) {
        if (context == null)
            return null;

        File path = null;
        if (sdcard && SDCardUtils.isSDCardEnable()) {
            path = context.getExternalFilesDir(type);
        } else {
            path = new File(context.getFilesDir(), type);
        }
        if (path != null) {
            File parent = path.getParentFile();
            if (!parent.exists())
                parent.mkdirs();
            if (!path.exists())
                path.mkdir();
        }
        return path;
    }

    public static File getCacheDir(Context context) {
        if (context == null)
            return null;

        File path = context.getExternalCacheDir();
        if (path == null || !path.exists()) {
            path = context.getCacheDir();
        }
        return path;
    }

    public static String readFile(String path) {
        File file = new File(path);
        if (file != null && file.exists()) {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return builder.toString();
        }
        return null;
    }

    public static ArrayList<File> listFiles(String path) {
        File file = new File(path);
        if (SDCardUtils.isSDCardEnable()) {
            File[] files = file.listFiles();
            if (files != null)
                return new ArrayList<File>(Arrays.asList(files));
        }
        return null;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();

        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();

        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (String element : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + element);
            } else {
                temp = new File(path + File.separator + element);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + element);
                delFolder(path + "/" + element);
            }
        }
    }

    /**
     * 通过绝对路径删除某个文件
     *
     * @param path String 文件路径 如 c:/fqf/1.txt
     */
    public static void delFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void unzip(String zipFileName, String outputDirectory) throws Exception {
        // 采用直接覆盖的方式
        // delFolder(outputDirectory+"/www");
        // delFolder(outputDirectory+"/conf");

        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
        ZipEntry z;
        String name = "";
        int counter = 0;

        while ((z = in.getNextEntry()) != null) {
            name = z.getName();
            if (z.isDirectory()) {
                name = name.substring(0, name.length() - 1);
                File folder = new File(outputDirectory + File.separator + name);
                folder.mkdirs();
                if (counter == 0) {
                    // String extractedFile = folder.toString();
                }
                counter++;
            } else {
                File file = new File(outputDirectory + File.separator + name);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                int ch;
                byte[] buffer = new byte[1024];
                while ((ch = in.read(buffer)) != -1) {
                    out.write(buffer, 0, ch);
                    out.flush();
                }
                out.close();
            }
        }

        in.close();

    }

    public static void save(String path, InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) {
            if (!parent.exists())
                parent.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte[] buff = new byte[1024 * 8];
        int length;
        while ((length = bis.read(buff)) > -1) {
            bos.write(buff, 0, length);
        }
        bis.close();
        bos.close();
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static void copyFile(File src, File dest) throws Exception {
        int length;
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dest);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return;
            }
            if ((inC.size() - inC.position()) < 20971520)
                length = (int) (inC.size() - inC.position());
            else
                length = 20971520;
            inC.transferTo(inC.position(), length, outC);
            inC.position(inC.position() + length);
        }
    }
}
