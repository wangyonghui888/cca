package com.panda.sport.merchant.common.utils;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 字符串工具类
 * @Date: 2019-08-29 下午2:32:24
 */
public class StringUtil {


    //清除数字和空格
    private static Pattern SPACE_PATTERN = Pattern.compile("\\d|\\s");
    //过滤emoji 或者 其他非文字类型的字符
    private static Pattern EMOJI_PATTERN = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    //去除字符串中的空格、回车、换行符、制表符
    private static Pattern ENTER_PATTERN = Pattern.compile("\\s*|\t|\r|\n");
    /**
     * 匹配&或全角状态字符或标点
     */
    public static final String PATTERN = "&|[\uFE30-\uFFA0]|‘’|“”";

    public static String replaceSpecialtyStr(String str, String pattern, String replace) {
        if (isBlankOrNull(pattern)) {
            pattern = "\\s*|\t|\r|\n";//去除字符串中空格、换行、制表
        }
        if (isBlankOrNull(replace)) {
            replace = "";
        }
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }

    public static boolean isBlankOrNull(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlankOrNull(str);
    }

    /**
     * 判断字符串是否是json字符串
     * @param str
     * @return
     */
    public static boolean isJsonStr(String str) {
        if (isBlankOrNull(str)) {
            return false;
        }
        try {
            JSON.parse(str);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 生成随机数
     * @param cnt  个数
     * @param numberOnly  是否仅数字
     * @return
     */
    public static String genRandomCode(int cnt, boolean numberOnly) {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int scope = numberOnly ? 10 : chars.length();
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        for (int i = 0; i < cnt; i++) {
            int index = r.nextInt(scope);
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 清除数字和空格
     */
    public static String cleanBlankOrDigit(String str) {
        if (isBlankOrNull(str)) {
            return "null";
        }
        return SPACE_PATTERN.matcher(str).replaceAll("");
    }

    /**
     * 转换为16进制
     *
     * @param array
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static byte[] convertStreamToByteArray(InputStream is) {
        try {
            return convertStreamToString(is).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }


    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (org.apache.commons.lang3.StringUtils.isBlank(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    /**
     * 字节数组转化为16进制字符串
     * @param bytes
     * @return
     */
    public static String byteArrayToHexString(byte[] bytes) {
        return byteArrayToHexString(bytes, bytes.length);
    }

    private static String byteArrayToHexString(byte[] bytes, int length) {
        if (bytes == null || length > bytes.length) {
            return null;
        }
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String hv = Integer.toHexString(bytes[i] & 0xFF);
            if (hv.length() < 2) {
                strb.append(0);
            }
            strb.append(hv);
        }
        return strb.toString();
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        Matcher emojiMatcher = EMOJI_PATTERN.matcher(source);

        if (emojiMatcher.find()) {
            return (emojiMatcher.replaceAll("")).replace("?", "");
        }
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            } else {
            }
        }
        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }



    /**
     *

     *  去掉指定字符串的开头和结尾的指定字符
     *

     *
     * @param stream 要处理的字符串
     * @param trimstr 要去掉的字符串
     * @return 处理后的字符串
     */
    public static String sideTrim(String stream, String trimstr) {
        // null或者空字符串的时候不处理
        if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
            return stream;
        }

        // 结束位置
        int epos = 0;

        // 正规表达式
        String regpattern = "[" + trimstr + "]*+";
        Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);

        // 去掉结尾的指定字符
        StringBuffer buffer = new StringBuffer(stream).reverse();
        Matcher matcher = pattern.matcher(buffer);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
        }

        // 去掉开头的指定字符
        matcher = pattern.matcher(stream);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            stream = stream.substring(epos);
        }

        // 返回处理后的字符串
        return stream;
    }

    /*
    类似jdk 1.8 的String.join
     */
    public static String join(String delimiter, List<String> strList){
        StringBuffer sb = new StringBuffer();
        int idx = 1;
        for (String str : strList) {
            if (idx == strList.size()) {
                sb.append(str);
            } else {
                sb.append(str).append(delimiter);
            }
            idx++;
        }

        return new String(sb);
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Matcher m = ENTER_PATTERN.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 生成随机num位数
     * @param num
     * @return
     */
    public static String randomCode(int num){
        String code = "";
        for(int i = 0 ; i < num ; i++){
            java.util.Random random=new java.util.Random();// 定义随机类
            int result=random.nextInt(10);
            code = code + result;
        }
        return code;
    }



}
