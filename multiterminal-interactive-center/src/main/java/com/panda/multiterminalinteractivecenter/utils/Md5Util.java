package com.panda.multiterminalinteractivecenter.utils;


import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Md5Util {

    public static boolean checkMd5(String str, String key, String md5) {
        return getMD5(str, key).equals(md5);
    }

    public static String getMD5(String str, String key) {
        return getMD5Value(getMD5Value(str) + "&" + key);
    }

    /**
     * 获取MD5的值，可用于对比校验
     *
     * @param sourceStr
     * @return
     */
    private static String getMD5Value(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("获取MD5异常!", e);
        }
        return result;
    }




}
