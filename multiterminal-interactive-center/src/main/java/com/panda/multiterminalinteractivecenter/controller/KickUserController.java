package com.panda.multiterminalinteractivecenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.service.impl.KickUserLogServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.KickUserServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.MaintenanceLogServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.MaintenancePlatformServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.KickUserLogPageVo;
import com.panda.multiterminalinteractivecenter.vo.KickUserVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceLogVo;
import com.panda.multiterminalinteractivecenter.vo.ServeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.controller
 * @Description :  TODO
 * @Date: 2022-03-19 13:08:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@RestController
@RequestMapping("/kickUser")
@Slf4j
public class KickUserController {

    @Autowired
    private MaintenancePlatformServiceImpl platformService;

    @Autowired
    private KickUserServiceImpl kickUserService;

    @Autowired
    private KickUserLogServiceImpl kickUserLogService;

    @Autowired
    private MaintenanceLogServiceImpl maintenanceLogService;

    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @PostMapping("/getServes")
    public APIResponse<List<ServeVo>> getServes(@RequestBody KickUserVo kickUserVo){
        log.info("获取服务列表集合参数={}", JSONObject.toJSONString(kickUserVo));
        return APIResponse.returnSuccess(platformService.getServes(kickUserVo));
    }

    @PostMapping("/kickOut")
    public APIResponse<?> kickUser(HttpServletRequest request, @RequestBody KickUserVo kickUserVo){
        try {
            String ipAddr = IPUtils.getIpAddr(request);
            kickUserService.kickUser(kickUserVo,ipAddr);
            if (kickUserVo.getIndex() != null && kickUserVo.getIndex() == 0){
                //首页来得记个额外日志
                String token = request.getHeader("token");
                String username = JWTUtil.getUsername(token);
                saveMaintenanceLogVo(kickUserVo.getSystemId(),username, Constant.OperationType.KICKUSER.getValue(),"踢用户",ipAddr);
            }
        }catch (Exception e){
            log.error("踢出用户调用异常！参数 = {}", JSON.toJSONString(kickUserVo), e);
            return APIResponse.returnFail(e.getMessage());
        }
        return APIResponse.returnSuccess();
    }


    @PostMapping("/kickOutAll")
    public APIResponse<?> kickOutAll(HttpServletRequest request,@RequestBody KickUserVo kickUserVo){
        try {
            log.info("踢出全部用户调用异常！参数 = {}", JSON.toJSONString(kickUserVo));
            if (StringUtils.isEmpty(kickUserVo.getDataCode())){
                return APIResponse.returnFail("dataCode不能为空！");
            }
            String ipAddr = IPUtils.getIpAddr(request);
            kickUserService.kickUserAll(kickUserVo,ipAddr);
        }catch (Exception e){
            log.error("踢出全部用户调用异常！参数 = {}", JSON.toJSONString(kickUserVo), e);
            return APIResponse.returnFail(e.getMessage());
        }
        return APIResponse.returnSuccess();
    }

    @PostMapping("/kickUserLog")
    public APIResponse<?> kickUserLog(@RequestBody KickUserLogPageVo pageVo){
        return kickUserLogService.pageList(pageVo);
    }

    //保存日志
    private void saveMaintenanceLogVo(Long platformId,String username,int operationType,String operationContent, String ipAddr){
        MaintenanceLogVo maintenanceLogVo = new MaintenanceLogVo();
        MaintenancePlatform maintenancePlatform = maintenancePlatformService.getById(platformId);
        maintenanceLogVo.setDataCode(maintenancePlatform.getDataCode());
        maintenanceLogVo.setOperationType(operationType);
        maintenanceLogVo.setOperators(username);
        maintenanceLogVo.setServerName(maintenancePlatform.getServerName());
        maintenanceLogVo.setOperationContent(maintenancePlatform.getServerName()+","+operationContent);
        maintenanceLogVo.setOperationIp(ipAddr);
        maintenanceLogService.save(maintenanceLogVo);
    }
}
