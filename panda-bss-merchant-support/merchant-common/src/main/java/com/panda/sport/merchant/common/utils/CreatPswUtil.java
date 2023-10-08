package com.panda.sport.merchant.common.utils;

public class CreatPswUtil {
    /**
     * 随机生成指定位数随机位数密码
     * @param
     * @return String
     */
    public static String getPsw(int len) {
        // 1、定义基本字符串baseStr和出参password
        StringBuilder password = null;
        String baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*";
        boolean flag = false;
        // 2、使用循环来判断是否是正确的密码
        while (!flag) {
            // 密码重置
            password = new StringBuilder();
            // 个数计数
            int a = 0, b = 0, c = 0, d = 0;
            for (int i = 0; i < len; i++) {
                int rand = (int) (Math.random() * baseStr.length());
                password.append(baseStr.charAt(rand));
                if (rand < 10) {
                    a++;
                }
                if (10 <= rand && rand < 36) {
                    b++;
                }
                if (36 <= rand && rand < 62) {
                    c++;
                }
                if (62 <= rand) {
                    d++;
                }
                if (a * b * c * d != 0) {
                    break;
                }
            }
            // 是否是正确的密码（4类中仅一类为0，其他不为0）
            flag = (a * b * c != 0 && d == 0) || (a * b * d != 0 && c == 0) || (a * c * d != 0 && b == 0)
                    || (b * c * d != 0 && a == 0);

        }
        return password.toString();
    }

}
