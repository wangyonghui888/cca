package com.panda.sport.merchant.api.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
public class AesUtil {

    /**
     * 默认的字符编码
     */
    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 算法
     */
    private static String ALGORITHM = "AES";

    private static final String KEY = "OBTY20220712OBTY";


    /**
     * 算法/模式/填充
     **/
    private static final String CipherMode = "AES/ECB/PKCS5Padding";


    private AesUtil() {
    }

    /**
     * 解密AES 32位
     *
     * @param sSrc      解密的内容
     * @return 解密后的明文 数据
     */
    public static String decrypt(String sSrc) {
        try {
            byte[] raw = KEY.getBytes(DEFAULT_CHARSET);
            SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // 先用base64解密
            byte[] encryptedArr = Base64.getDecoder().decode(sSrc);
            byte[] original = cipher.doFinal(encryptedArr);
            return new String(original, DEFAULT_CHARSET);
        } catch (Exception ex) {
            log.error("AES解密失败", ex);
            return null;
        }
    }


    /**
     * 加密32位
     *
     * @param sSrc 需要加密的内容
     * @return 加密的内容
     */
    public static String encrypt(String sSrc) {
        try {
            byte[] raw = KEY.getBytes(DEFAULT_CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(DEFAULT_CHARSET));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            log.error("AES加密失败", ex);
            return null;
        }
    }

    /**重构方法，list加密，逗号分割*/
    public static String encrypt(List<String> sSrc) {
        if(CollectionUtils.isEmpty(sSrc)){
            return "";
        }
        return encrypt(String.join(",", sSrc));
    }

}
