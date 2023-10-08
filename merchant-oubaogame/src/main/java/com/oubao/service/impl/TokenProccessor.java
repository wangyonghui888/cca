package com.oubao.service.impl;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author :  kiu
 * @version: V1.1.0
 * @Project Name :panda-bss
 * @Package Name :com.panda.sports.bss.lottery.controller
 * @Description : 生成Token的工具类
 * @since: 2019-10-16 17:32
 */
@Slf4j
public class TokenProccessor {
    private static final String SECRET = "9a96349e12345385785e804e0f4254dee";

    /**
     * 生成Token
     *
     * @return
     */
    public static String makeToken(Long userId) {
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + String.valueOf(userId) + SECRET;
        try {
            byte[] bytes = token.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(bytes);
            bytes = messageDigest.digest();
            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < bytes.length; i++) {
                shaHex = Integer.toHexString(bytes[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            //return new BigInteger(1, bytes).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
