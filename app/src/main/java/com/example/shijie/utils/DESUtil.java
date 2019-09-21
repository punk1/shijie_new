package com.example.shijie.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author L
 * @desc 加解密工具，用于保存密码
 */
public class DESUtil {
    // Android端DES对称加密解密 封装方法

    // 加密密码
    static String key = "pengthin";

    // 解密
    public static String Decryption(String message) {
        byte[] bytesrc;
        try {
            bytesrc = Base64.decode(message.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte).trim();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    // 替换并加密
    public static String Encryption(String message) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptbyte = cipher.doFinal(message.getBytes());
            String a = new String(Base64.encode(encryptbyte, Base64.DEFAULT));
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //只进行加密
    @SuppressLint("TrulyRandom")
    public static String EncryptionOnly(String message) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encryptbyte = cipher.doFinal(message.getBytes());
            String a = new String(Base64.encode(encryptbyte, Base64.DEFAULT));
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
