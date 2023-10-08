package com.panda.sport.merchant.common.utils;

import com.panda.sport.merchant.common.constant.Constant;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    private static Pattern PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    public static Matcher doMatcher(String source, String regex) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(source);
    }

    /**
     * @param htmlStr
     * @return 删除Img标签
     */
    public static String delImgTag(String htmlStr) {


        if (StringUtils.isEmpty(htmlStr)) {
            return null;
        }
        String regImg = "<img[^>]+>";
        Pattern pHtml = Pattern.compile(regImg, Pattern.CASE_INSENSITIVE);
        Matcher mHtml = pHtml.matcher(htmlStr);
        // 过滤html标签
        htmlStr = mHtml.replaceAll("");
        return htmlStr;
    }


    /**
     * 删除所有的html的标签
     *
     * @param str
     * @return
     */
    public static String delHtmlTag(String str) {

        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String htmlStr = str.replaceAll("\"", "'");
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr;
    }

    /**
     * 判断整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return PATTERN.matcher(str).matches();
    }

    /**
     * 是否是英文
     *
     * @param str
     * @return
     */

    public static boolean isEnglish(String str) {
        return str.matches("^[a-zA-Z]*");
    }

    public static boolean isLanguage(String str, String language) {
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            return isChinese(str);
        } else if (language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
            return str.matches("^[a-zA-Z]*");
        }
        return isChinese(str);
    }

    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find())
            return true;
        else
            return false;
    }

}
