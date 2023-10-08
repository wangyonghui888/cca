package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ResponseEnum;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.impl.RoleServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.UserRoleServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.UserServiceImpl;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.UserVo;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.controller
 * @Description :  TODO
 * @Date: 2022-04-29 13:04:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UserRoleServiceImpl userRoleService;
    @Autowired
    private MerchantLogService merchantLogService;

    @PostMapping(value = "/queryUserList")
    public APIResponse<Object> queryUserList(@RequestBody UserVo userVo){
        return userService.queryList(userVo);
    }

    @PostMapping(value = "/updatePassword")
    public APIResponse<Object> updatePassword(HttpServletRequest request, @RequestBody UserVo userVo){
        User user = userService.getUser(userVo.getUsername(),userVo.getOldPassword());
        if (user == null){
            return APIResponse.returnFail("旧的账号密码错误！");
        }
        if (StringUtils.isEmpty(userVo.getNewPassword()) || userVo.getNewPassword().length() < 6){
            return APIResponse.returnFail("新密码不能为空且长度不低于6位数！");
        }
        User update = new User();
        update.setId(user.getId());
        update.setPassword(userVo.getNewPassword());
        userService.updateUser(update);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("password"), "***", "***");
        merchantLogService.saveLog(MerchantLogPageEnum.USER_MANAGE, MerchantLogTypeEnum.UPDATE_PASSWORD, filedVO,  null, String.valueOf(user.getId()), request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/restKey")
    public APIResponse<Object> restKey(HttpServletRequest request, @RequestBody UserVo userVo){
        if (userVo.getId() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        userService.restKey(userVo.getId(), request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/addUser")
    public APIResponse<Object> addUser(HttpServletRequest request, @RequestBody UserVo userVo){
        if (userVo.getUsername() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        User u = userService.findUserByName(userVo.getUsername());
        if (u != null){
            return APIResponse.returnFail("用户已存在！");
        }
        User user = new User();
        user.setName(userVo.getUsername());
        user.setPassword(getRandomPassword(8));
        user.setCreatTime(new Date());
        userService.addUser(user, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/enableUser")
    public APIResponse<Object> enableUser(@RequestBody UserVo userVo){
        if (userVo.getId() == null || userVo.getIsEnable() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        User update = new User();
        update.setId(userVo.getId());
        update.setIsEnable(userVo.getIsEnable());
        userService.updateUser(update);
        return APIResponse.returnSuccess();
    }


    /**
     * 返回随机产生的8位数
     */
    public static String getRandomPassword(int len) {
        String result = makeRandomPassword(len);
        if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") &&
                result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
            return result;
        }
        result = makeRandomPassword(len);
        return result;
    }

    /**
     * 产生8位随机数
     *
     * @param len 长度
     * @return
     */
    public static String makeRandomPassword(int len) {
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }

    @GetMapping(value = "/queryUserRoleList")
    public APIResponse<Object> queryUserRoleList(@RequestParam(value = "userId",required = true)Long roleId){
        return roleService.queryUserRoleList(roleId);
    }

    @PostMapping(value = "/addUserRole")
    public APIResponse<Object> addUserRole(HttpServletRequest request, @RequestBody UserVo userVo){
        if (userVo.getId() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        userRoleService.addUserRoles(userVo, request);
        return APIResponse.returnSuccess();
    }
}
