package com.hfbj.hyb.utils;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * AES 加解密
 * @author LiYanZhao
 * @date 14-11-19 下午4:26
 */
public class AESHelper {
    private static final String ALGORITHM = "AES/CBC/NoPadding";

    public static String decrypt(String data) {
        try {
            String key = "2014-11-21 14";
            String iv = "1234567812345678";
            byte[] encrypted1 = Base64.decode(data, Base64.DEFAULT);
            // byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(getRawKey(key.getBytes()), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getRawKey(byte[] password) {
        int keysize = 16;
        byte[] key = new byte[keysize];

        int len = password.length > keysize ? keysize : password.length;
        System.arraycopy(password, 0, key, 0, len);

        return key;
    }

    public static String encrypt(String content){
        try {
            //String key = "1234567812345678";
            String key = "2014-11-21 14";
            String iv = "1234567812345678";

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = content.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(getRawKey(key.getBytes()), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return Base64.encodeToString(encrypted, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * AES 加密
     * @param content
     * @param password
     * @return
     */
    public static String encrypt(String content,String password) {
        SecretKeySpec spec = getSecretKeySpec(password);
        try {

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,spec);
            byte[] result = cipher.doFinal(content.getBytes("utf-8"));

            return byte2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
         }

        return null;
    }

    /**
     * AES 解密
     * @param encrypt
     * @param password
     * @return
     */
    public static String decrypt(String encrypt,String password){
        try {
            SecretKeySpec spec = getSecretKeySpec(password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,spec);
            byte[] result = cipher.doFinal(hexStr2Byte(encrypt));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * byte[] 转 十六进制字符串
     * @param bytes
     * @return
     */
    private static String byte2HexStr(byte[] bytes){
        StringBuffer buffer = new StringBuffer();
        for(byte b : bytes){
            String hex = Integer.toHexString(b & 0xFF);
            if(hex.length() == 1){
                hex = '0' + hex;
            }
            buffer.append(hex);
        }
        return buffer.toString();
    }


    /**
     * 十六进制字符 转 byte[]
     * @param hexStr
     * @return
     */
    private static byte[] hexStr2Byte(String hexStr){
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
       return result;
    }



    /**
     * 获取秘钥
     * @param password
     * @return
     */
    private static SecretKeySpec getSecretKeySpec(String password){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128,new SecureRandom(password.getBytes()));
            SecretKey secretKey = keyGen.generateKey();
            byte[] encodeFormat = secretKey.getEncoded();
            return new SecretKeySpec(encodeFormat,ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
