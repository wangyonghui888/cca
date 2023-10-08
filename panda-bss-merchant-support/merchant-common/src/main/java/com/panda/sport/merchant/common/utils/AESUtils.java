package com.panda.sport.merchant.common.utils;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class AESUtils {

    private static final String encodeRules = "b34SszENv6cQ5NeOUufLwbbRpkVhf5VH";
    private static final String AES = "AES";
    private static final String SHA_1_PRNG = "SHA1PRNG";
    private static final String UTF_8 = "utf-8";

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String aesEncode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance(SHA_1_PRNG);
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            SecretKey original_key = keygen.generateKey();
            byte[] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byte_encode = content.getBytes(UTF_8);
            byte[] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = Base64.getEncoder().encodeToString(byte_AES);
            return AES_encode;
        } catch (Exception e) {
            log.error("加密数据失败，异常信息", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String aesDecode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance(SHA_1_PRNG);
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            SecretKey original_key = keygen.generateKey();
            byte[] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, UTF_8);
            return AES_decode;
        } catch (Exception e) {
            log.error("解密数据失败，异常信息", e);
            throw new RuntimeException(e);
        }
    }

    /*public static void main(String[] args) {
        String decodeIdnumber = AESUtils.aesDecode("JFAbFql2MkfKeyGaIjB7/qXCdraFZsjsPlOeAHoFGbw=\n");

        System.out.println("解密后身份证号/" + decodeIdnumber);

       String ss ="111111&1692614752000";
       String sing =Md5Util.getMD5(ss,decodeIdnumber);
        System.out.println("加签/" + sing);
    }*/

}