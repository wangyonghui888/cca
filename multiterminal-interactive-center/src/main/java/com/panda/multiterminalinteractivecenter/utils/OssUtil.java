package com.panda.multiterminalinteractivecenter.utils;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.entity.Oss;
import com.panda.multiterminalinteractivecenter.enums.DomainTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.GroupTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Data
public class OssUtil {

    private static String keys = "panda1234_1234ob";


    /**
     * 解析并组装oss
     */

    public static List<Oss> parseBuildOss(JSONObject obj ){
        List<Oss> ossList = new ArrayList<>();
        Oss oss;
        JSONObject gbb = (JSONObject) obj.get("GAB");
        List<String> gbbApi = (List<String>) gbb.get("api");
        for (String str : gbbApi) {
            String api = OssUtil.decryptAES(str);
            if(StringUtils.isNotEmpty(api)){
                oss = new Oss();
                oss.setDomainType(DomainTypeEnum.APP.getCode());
                oss.setGroupType(GroupTypeEnum.B_GROUP.getGroupType());
                oss.setMerchantTypeName("GAB");
                oss.setEncryptDomainName(str);
                oss.setDomainName(api);
                ossList.add(oss);
            }
        }


        JSONObject gas = (JSONObject) obj.get("GAS");
        List<String> gasApi = (List<String>) gas.get("api");
        for (String str : gasApi) {
            String api = OssUtil.decryptAES(str);
            if(StringUtils.isNotEmpty(api)){
                oss = new Oss();
                oss.setDomainType(DomainTypeEnum.APP.getCode());
                oss.setGroupType(GroupTypeEnum.S_GROUP.getGroupType());
                oss.setMerchantTypeName("GAS");
                oss.setEncryptDomainName(str);
                oss.setDomainName(api);
                ossList.add(oss);
            }
        }

        JSONObject gay = (JSONObject) obj.get("GAY");
        List<String> gayApi = (List<String>) gay.get("api");
        for (String str : gayApi) {
            String api = OssUtil.decryptAES(str);
            if(StringUtils.isNotEmpty(api)){
                oss = new Oss();
                oss.setDomainType(DomainTypeEnum.APP.getCode());
                oss.setGroupType(GroupTypeEnum.Y_GROUP.getGroupType());
                oss.setMerchantTypeName("GAY");
                oss.setEncryptDomainName(str);
                oss.setDomainName(api);
                ossList.add(oss);
            }
        }

        JSONObject gaCommon = (JSONObject) obj.get("GACOMMON");
        List<String> gaCommonApi = (List<String>) gaCommon.get("api");
        for (String str : gaCommonApi) {
            String api = OssUtil.decryptAES(str);
            if(StringUtils.isNotEmpty(api)){
                oss = new Oss();
                oss.setDomainType(DomainTypeEnum.APP.getCode());
                oss.setGroupType(GroupTypeEnum.C_GROUP.getGroupType());
                oss.setMerchantTypeName("GACOMMON");
                oss.setEncryptDomainName(str);
                oss.setDomainName(api);
                ossList.add(oss);
            }
        }

        return ossList;
    }


    /**
     * PKCS5Padding -- Pkcs7 两种padding方法都可以
     *
     * @param content content
     */
    public static String decryptAES(String content) {
        try {
            byte[] decode = Base64.getDecoder().decode(content);
            SecretKeySpec secretKe = new SecretKeySpec(keys.getBytes(StandardCharsets.UTF_8), "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKe);
            //解密后是16进制
            return new String(cipher.doFinal(decode));
        } catch (Exception e) {
            log.error(String.format("解密失败:，content：%s, key: %s", content, keys));
            throw new RuntimeException("decrypt error");
        }

    }

    /**
     * 加密
     *
     * @param content content
     */
    public static String encryptAES(String content) {
        try {
            SecretKeySpec secretKe = new SecretKeySpec(keys.getBytes(StandardCharsets.UTF_8), "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKe);
            byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error(String.format("加密失败:，content：%s, key: %s", content, keys));
            throw new RuntimeException("encrypt error");
        }

    }


    public static String getMd5key(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }
}
