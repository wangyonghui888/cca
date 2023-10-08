package com.panda.sport.merchant.common.utils;

import cn.hutool.crypto.digest.DigestUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author :  ives
 * @Description :  体育跟电竞交互所需工具类
 * @Date: 2022-04-02 16:51
 */
public class SportToESportUtil {

    public static String eSportNoticeEncrypt(Map<String,Object> sourceMap, String key){
        Map<String,Object> map = new TreeMap<>(sourceMap);
        map.put("key",key);
        String result;
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> obj : map.entrySet()) {
            stringBuilder.append(obj.getKey()).append("=").append(obj.getValue()).append("&");
        }
        if (stringBuilder.length() < 1){
            return null;
        }
        String preStr = stringBuilder.substring(0,stringBuilder.length() - 1 );
        String sign = DigestUtil.md5Hex(preStr);

        result = interferenceSign(sign).toLowerCase();
        return result;
    }

    /**
     * 给sign值添加干扰值,头尾，9,17位置插入随机值
     * @param sign
     * @return String
     */
    public static String interferenceSign(String sign){
        String result = new StringBuilder().append(RandomStringGenerator.getRandomStringByLength(2)).append(sign.substring(0,9))
                .append(RandomStringGenerator.getRandomStringByLength(2)).append(sign.substring(9,17))
                .append(RandomStringGenerator.getRandomStringByLength(2)).append(sign.substring(17))
                .append(RandomStringGenerator.getRandomStringByLength(2)).toString();
        return result;
    }
}
