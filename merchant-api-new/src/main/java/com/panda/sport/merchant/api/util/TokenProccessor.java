package com.panda.sport.merchant.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

@Slf4j
public class TokenProccessor {

    private static final String SECRET = "9a96349e12345385785e804e0f4254dee";

    private static String ISSUER = "panda_user";

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

    public static String generateToken(Long userId, String key) {
        Date now = new Date();
        Date expire = DateUtils.addMinutes(now, 5);
        return JWT.create().withAudience(userId + "").withIssuedAt(now).withExpiresAt(expire).sign(Algorithm.HMAC256(key));
    }

    public boolean verifyToken(String key, String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(key)).build();
        jwtVerifier.verify(token);
        return true;
    }

    public static String encrypt(String str) {
        try {
            byte[] bytes = str.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(bytes);
            bytes = messageDigest.digest();

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, bytes).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}