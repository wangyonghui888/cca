package com.panda.sport.merchant.manage.service.impl;

import com.panda.sports.auth.rpc.IAuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  查询权限平台账号信息
 * @Date: 2020-09-02 15:48
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service
@Slf4j
public class LoginUserService {


    @Reference(check = false)
    private IAuthRequiredPermission iAuthRequiredPermission;

    public String getLoginUser(Integer userId) {
        try {
            Map<String, Object> result = iAuthRequiredPermission.getLoginUser(userId, 10008);
            if (result != null) {
                return (String) result.get("userCode");
            }
        } catch (Exception e) {
            log.error("调用权限平台用户账户接口异常！", e);
        }
        return null;
    }
}
