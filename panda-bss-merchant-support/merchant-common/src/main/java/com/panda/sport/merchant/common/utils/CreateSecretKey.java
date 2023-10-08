package com.panda.sport.merchant.common.utils;

import java.util.Random;

public class CreateSecretKey {

    private final static Random random = new Random();
    private final static int keyLength = 30;
    private final static String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*:_+<>?~#$@";

    /**
     * 生成16位AES随机密钥
     *
     * @return
     */
    public static String getAESRandomKey() {
        long longValue = random.nextLong();
        return String.format("%016x", longValue);
    }


    public static String keyCreate() {
        Random random = new Random();
        StringBuffer keysb = new StringBuffer();
        for (int i = 0; i < keyLength; i++) {
            int number = random.nextInt(base.length());
            keysb.append(base.charAt(number));
        }
        return keysb.toString();
    }

}
