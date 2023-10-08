package com.panda.sport.merchant.manage.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.util
 * @Description :  常用正则表达式字段验证
 * @Date: 2020-08-01 9:48
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
public class RegularUtil {

    /**
     * 验证ip合法性
     * @param ipstr
     * @return
     */
    public static boolean isIpv4(String... ipstr){
        String regIp = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$";

        for(String ip : ipstr){
            log.info("输入IP{}",ip);
            boolean flag = ip.matches(regIp);
            if(!flag){
                return false;
            }
        }
        return true;
    }
}
