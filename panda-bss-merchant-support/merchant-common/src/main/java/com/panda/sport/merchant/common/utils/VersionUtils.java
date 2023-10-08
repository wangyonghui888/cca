package com.panda.sport.merchant.common.utils;

import java.util.regex.Matcher;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 版本工具类
 * @Date: 2019-08-29 下午2:28:20
 */
public class VersionUtils {

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0 ，如果两个参数不符合版本号格式，则返回null。
     * @param version1
     * @param version2
     * @return
     */
    public static Integer compareVersion(String version1, String version2) {
        if (!isRightVesion(version1) || !isRightVesion(version2)) {
            return null;
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    private static boolean isRightVesion(String vesion) {
        if (vesion == null) {
            return false;
        }
        Matcher m = RegexUtils.doMatcher(vesion, "([\\da-zA-Z]+\\.?)+");
        if (m.matches()) {
            return true;
        }
        return false;
    }
}
