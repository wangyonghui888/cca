package com.oubao.vo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {

    public static String str;
    public static final String EMPTY_STRING = "";

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(origin
                    .getBytes("UTF-8")));
        } catch (Exception ex) {
        }
        return resultString;
    }

    public static String getMD5(String str, String key) {
        return getMD5Value(getMD5Value(str) + "&" + key);
    }
    public static boolean checkMd5(String str, String key, String md5) {
        return getMD5(str, key).equals(md5);
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
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
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
            e.printStackTrace();
        }
        return result;
    }
}
