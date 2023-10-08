package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.manage.service.ISystemSwitchService;
import com.panda.sport.merchant.manage.service.impl.LoginUserService;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/systemSwitch")
@Slf4j
public class SystemSwitchController {

    @Autowired
    private ISystemSwitchService systemSwitchService;

    @Autowired
    LoginUserService loginUserService;

    private static final String CONFIG_KEY = "chatRoomSwitch";

    @PostMapping("/updateSystemSwitch")
    public Response updateSystemSwitch(HttpServletRequest request, @RequestBody SystemSwitchVO systemSwitchVO){
        if(systemSwitchVO.getId() == null || StringUtils.isEmpty(systemSwitchVO.getConfigValue())){
            return Response.returnFail("参数异常!");
        }
        if(StringUtils.isNotEmpty(systemSwitchVO.getConfigKey())
                && systemSwitchVO.getConfigKey().equals(CONFIG_KEY)){
            if(systemSwitchVO.getPullMsgRate() == null){
                return Response.returnFail("参数异常!");
            }

        }
        Integer userId = SsoUtil.getUserId(request);
        String currentUser = loginUserService.getLoginUser(userId);
        systemSwitchVO.setUpdateBy(currentUser);
        try {
            systemSwitchService.updateSystemSwitch(systemSwitchVO,request);
        }catch (Exception e){
            return Response.returnFail(e.getMessage());
        }
        return Response.returnSuccess();
    }

    @PostMapping("/querySystemSwitch")
    public Response querySystemSwitch(){
        return Response.returnSuccess(systemSwitchService.querySystemSwitch());
    }
}
