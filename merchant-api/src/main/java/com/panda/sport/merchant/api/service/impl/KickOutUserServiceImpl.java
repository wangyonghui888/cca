package com.panda.sport.merchant.api.service.impl;

import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.panda.sport.merchant.api.util.RedisConstants.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.api.service.impl
 * @Description :  TODO
 * @Date: 2022-03-20 19:36:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
@Slf4j
public class KickOutUserServiceImpl {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    public RedisTemp redisTemp;

    @Async
    public void kickoutUserInternal(Long uid) {
        try {
            UserPO userPO = userMapper.getUserInfo(uid);
            if (userPO == null) {
                log.error("kickoutUserInternal.uid=" + uid);
            }
            cleaningRedisUserCache(uid, userPO.getUsername(), userPO.getMerchantCode());
        } catch (Exception e) {
            log.error("UserController.kickoutUserInternal,exception:", e);
        }
    }

    @Async
    public APIResponse kickoutMerchantUser(String merchantCode) {
        try {
            if (StringUtils.isNotEmpty(merchantCode)) {
                List<Map<String, Object>> userList = userMapper.queryUserIdList(merchantCode);
                if (CollectionUtils.isEmpty(userList)) {
                    return APIResponse.returnFail("kickoutMerchantUser商户失败!");
                }
                for (Map<String, Object> tempUser : userList) {
                    String userName = (String) tempUser.get("userName");
                    Long uid = (Long) tempUser.get("uid");
                    cleaningRedisUserCache(uid, userName, merchantCode);
                }
            } else {
                Set<String> userKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + "*");
                redisTemp.deleteKeys(userKey);
                Set<String> tokenKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + "*");
                redisTemp.deleteKeys(tokenKey);
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("踢出商户异常!", e);
            return APIResponse.returnFail("提出商户失败!");
        }
    }

    private void cleaningRedisUserCache(Long uid, String username, String merchantCode) {
        Set<String> userKey = new HashSet<>();
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":Mobile");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":MOBILE");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":mobile");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":PC");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":pc");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":H5");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":h5");
        Set<String> userNameKey = new HashSet<>();
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":Mobile");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":MOBILE");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":mobile");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":PC");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":pc");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":H5");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":h5");
        if (CollectionUtils.isNotEmpty(userKey)) {
            log.info("kickoutUserInternal:" + userKey + userNameKey);
            for (String user : userKey) {
                String token = redisTemp.get(user);
                redisTemp.delete(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token);
            }
            redisTemp.deleteKeys(userKey);
            redisTemp.deleteKeys(userNameKey);
        }
    }
}
