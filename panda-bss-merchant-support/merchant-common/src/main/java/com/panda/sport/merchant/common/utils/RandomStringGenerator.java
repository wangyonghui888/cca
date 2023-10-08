package com.panda.sport.merchant.common.utils;

import java.util.Random;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 生成random 字符串
 * @Date: 2019-08-29 下午2:18:47
 */
public class RandomStringGenerator {

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return getRandomString(length, base);
    }

    /**
     * 获取一定长度的随机字符串
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomDigitByLength(int length) {
        String base = "0123456789";
        return getRandomString(length, base);
    }

    private static String getRandomString(int length, String base) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


}
