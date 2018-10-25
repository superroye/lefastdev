package com.zzc.baselib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.zzc.baselib.base.AppBase;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtils {
    private static String signature;

    public static String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        String str;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i) {
                    str = new String(arrayOfChar2);
                    break;
                }
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception localException) {
            str = null;
        }
        return str;
    }

    private static Signature[] getRawSignature(Context paramContext) {
        PackageManager localPackageManager = paramContext.getPackageManager();
        PackageInfo localPackageInfo;
        try {
            localPackageInfo = localPackageManager.getPackageInfo(paramContext.getPackageName(), PackageManager.GET_SIGNATURES);
            if (localPackageInfo != null)
                return localPackageInfo.signatures;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
        }

        return null;
    }

    public static String get() {
        if (signature == null || signature.isEmpty())
            init(AppBase.app);
        return signature;
    }

    public static String getSHA1(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static void init(Context context) {
        Signature[] arrayOfSignature = getRawSignature(context);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0))
            return;
        signature = getMessageDigest(arrayOfSignature[0].toByteArray());
    }
}
