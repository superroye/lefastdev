package com.zzc.baselib.util;

import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) {
		 StringBuilder sb = new StringBuilder(b.length * 2);
		 for (byte element : b) {  
		     sb.append(HEX_DIGITS[(element & 0xf0) >>> 4]);  
		     sb.append(HEX_DIGITS[element & 0x0f]);  
		 }  
		 return sb.toString();  
	}

	public static String md5sum(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try{
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());	
		} catch (Exception e) {
			Log.v("MD5", "Error!");
			return null;
		}
	}
	
	private static MessageDigest messageDigest;
	
	public synchronized static String toMd5(String string) {

	    byte[] hash = null;
	    
	    try {
	    	if(messageDigest == null){
		    	messageDigest = MessageDigest.getInstance("MD5");
		    }
	    	messageDigest.reset();  
            messageDigest.update(string.getBytes("UTF-8")); 
	        hash = messageDigest.digest();

	    } catch (NoSuchAlgorithmException e) {

	        throw new RuntimeException("Huh, MD5 should be supported?", e);

	    } catch (UnsupportedEncodingException e) {

	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);

	    }

	    StringBuffer md5StrBuff = new StringBuffer();
	    
        for (int i = 0; i < hash.length; i++) {                
            if (Integer.toHexString(0xFF & hash[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & hash[i]));
            else    
                md5StrBuff.append(Integer.toHexString(0xFF & hash[i]));
        }    
    
        return md5StrBuff.toString();   

	    /*StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {

	        if ((b & 0xFF) < 0x10) hex.append("0");

	        hex.append(Integer.toHexString(b & 0xFF));

	    }

	    return hex.toString();*/

	}
	
	
	
}
