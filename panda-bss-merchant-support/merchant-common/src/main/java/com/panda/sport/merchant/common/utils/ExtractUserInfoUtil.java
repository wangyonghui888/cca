package com.panda.sport.merchant.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.order.utils
 * @Description :  扩展用户类
 * @Date: 2021-09-10 11:29
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public abstract class ExtractUserInfoUtil {
    /**
     * 获取扩展用户信息
     * @param request
     * @return
     */
    public static Map<String, String> extractUserInfo(HttpServletRequest request) {
        Map<String, String> userMap = Maps.newHashMap();
        String userId = request.getHeader("user-id");
        if (StringUtils.isBlank(userId)) {
            userId = "999";
        }
        String userName = request.getHeader("merchantName");
        if (StringUtils.isBlank(userId)) {
            userName = "system";
        }
        userMap.put("userId", userId);
        userMap.put("userName", userName);
        return userMap;
    }
}
