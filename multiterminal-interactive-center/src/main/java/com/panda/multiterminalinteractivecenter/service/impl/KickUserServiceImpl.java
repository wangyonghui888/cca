package com.panda.multiterminalinteractivecenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.dto.KickUserDto;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.enums.KickUserTypeEnum;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.mapper.MaintenancePlatformMapper;
import com.panda.multiterminalinteractivecenter.mapper.MaintenanceRecordMapper;
import com.panda.multiterminalinteractivecenter.utils.MD5Utils;
import com.panda.multiterminalinteractivecenter.vo.DjKickUserVo;
import com.panda.multiterminalinteractivecenter.vo.KickUserVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceLogVo;
import com.panda.multiterminalinteractivecenter.vo.TyKickUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-20 15:00:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
@Slf4j
@RefreshScope
public class KickUserServiceImpl {

    @Autowired
    private KickUserLogServiceImpl kickUserLogService;

    @Autowired
    private MaintenancePlatformMapper maintenancePlatformMapper;

    @Autowired
    private MaintenanceRecordMapper maintenanceRecordMapper;

    @Autowired
    private ESportSendServiceImpl djSendService;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MaintenanceLogServiceImpl maintenanceLogService;

    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @Autowired
    private LotterySendServiceImpl lotterySendService;

    private static String ENCRY = "fhjiosdfhjeiowfjmeowdpfkdsfewf";

    public void kickUserAll(KickUserVo kickUserVo,String ipAddr) {
        List<MaintenanceRecord>  list = maintenanceRecordMapper.getRuningPlatform(kickUserVo.getDataCode());
        for (MaintenanceRecord maintenanceRecord : list){
            MaintenancePlatform maintenancePlatform = maintenancePlatformMapper.selectById(maintenanceRecord.getMaintenancePlatformId());
            if (maintenancePlatform != null){
                if (StringUtils.isNotEmpty(maintenancePlatform.getKickUserType()) && maintenancePlatform.getKickUserType().contains("4")){
                    KickUserVo newKickUserVo = new KickUserVo();
                    newKickUserVo.setSystemId(maintenancePlatform.getId());
                    newKickUserVo.setKickType(4);
                    try {
                        kickUser(newKickUserVo,ipAddr);
                    }catch (Exception e){
                        log.error("踢出用户异常！", e);
                    }
                    User user = (User) SecurityUtils.getSubject().getPrincipal();
                    saveMaintenanceLogVo(newKickUserVo.getSystemId(),user.getName(), Constant.OperationType.KICKUSER.getValue(),"踢用户", ipAddr);
                }
            }
        }
    }

    //保存日志
    private void saveMaintenanceLogVo(Long platformId,String username,int operationType,String operationContent,String ipAddr){
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
    public void kickUser(KickUserVo kickUserVo,String ipAddr){
        log.info("踢出用户 star={} 请求参数={}",System.currentTimeMillis(), JSON.toJSONString(kickUserVo));
        MaintenancePlatform maintenancePlatform = maintenancePlatformMapper.selectById(kickUserVo.getSystemId());
        if (KickUserTypeEnum.allUser.getCode().equals(kickUserVo.getKickType())) {
            if (StringUtils.isEmpty(maintenancePlatform.getKickUserType()) || !maintenancePlatform.getKickUserType().contains("4")) {
                throw new RuntimeException(maintenancePlatform.getServerName() + "不支持全踢用户！");
            }
        }
        boolean result = true;
        if (KickUserTypeEnum.merchant.getCode().equals(kickUserVo.getKickType())){
            String merchantCodeStr = kickUserVo.getKickParam();
            kickUserVo.setKickParam(null);
            String[] merchantCodes = merchantCodeStr.split(",");
            for (String code : merchantCodes){
                kickUserVo.setMerchantCode(code);
                result = sendMsg(maintenancePlatform, kickUserVo);
            }
            kickUserVo.setMerchantCode(merchantCodeStr);
        }else {
            result = sendMsg(maintenancePlatform, kickUserVo);
        }
        if (result) {
            kickUserLogService.saveLog(maintenancePlatform.getServerName(), kickUserVo.getKickType(),ipAddr, kickUserVo, maintenancePlatform.getDataCode());
        }else {
            throw new RuntimeException("API调用异常，踢出失败！");
        }
    }


    private boolean sendMsg(MaintenancePlatform maintenancePlatform ,KickUserVo kickUserVo){
        boolean result = false;
        String url = maintenancePlatform.getBaseUrl() + maintenancePlatform.getKickUserUrl();
        switch (maintenancePlatform.getDataCode()){
            case "ty":
                try {
                    HttpClient httpClient = new HttpClient();
                    if ("ty_server".equals(maintenancePlatform.getServeCode())){
                        Object object = merchantApiClient.kickOutUserMerchant(kickUserVo.getMerchantCode(),kickUserVo.getKickParam());
                        log.info("体育服务调用成功！result= {}", JSON.toJSONString(object));
                        result = true;
                    }else {
                        TyKickUserVo userVo = new TyKickUserVo();
                        userVo.setMerchantCode(kickUserVo.getMerchantCode());
                        userVo.setUserIds(kickUserVo.getKickParam());
                        userVo.setAppId(maintenancePlatform.getServeCode());
                        userVo.setTime(String.valueOf(System.currentTimeMillis()));
                        userVo.setEncry(MD5Utils.MD5Encode(userVo.getAppId() + ENCRY + userVo.getTime()));
                        PostMethod postMethod = new PostMethod(url);
                        postMethod.setRequestHeader("Content-Type", "application/json;charset=utf-8");
                        RequestEntity entity = new StringRequestEntity(JSON.toJSONString(userVo), "application/json", "UTF-8");
                        postMethod.setRequestEntity(entity);
                        int response = httpClient.executeMethod(postMethod);
                        if (response == 200) {
                            result = true;
                            String postResult = new String(postMethod.getResponseBodyAsString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            log.info("体育服务调用成功！result= {}", postResult);
                        }
                    }
                }catch (Exception e){
                    log.error("体育服务={} 踢用户调用异常！参数={}", maintenancePlatform.getServerName(),JSON.toJSONString(kickUserVo), e);
                }
                break;
            case "cp":
                try {
                    KickUserDto kickUserDto = new KickUserDto();
                    kickUserDto.setType(kickUserVo.getKickType());
                    kickUserDto.setSystemCode(maintenancePlatform.getServeCode());
                    kickUserDto.setMerchantCodes(kickUserVo.getMerchantCode());
                    kickUserDto.setUserIds(kickUserVo.getKickParam());
                    lotterySendService.sendKickUser(kickUserDto,"/sabang/pulic/kickUser");
                    result = true;
                }catch (Exception e){
                    log.error("彩票踢出用户异常！", e);
                }
                break;
            case "dj":
                try {
                    DjKickUserVo djKickUserVo = new DjKickUserVo();
                    int type;
                    if (KickUserTypeEnum.allUser.getCode().equals(kickUserVo.getKickType())){
                        type = 1;
                    }else if (KickUserTypeEnum.merchant.getCode().equals(kickUserVo.getKickType())){
                        djKickUserVo.setMerchant_id(kickUserVo.getMerchantCode());
                        type = 2;
                    }else if (KickUserTypeEnum.user.getCode().equals(kickUserVo.getKickType())){
                        djKickUserVo.setMerchant_id(kickUserVo.getMerchantCode());
                        djKickUserVo.setMembers(kickUserVo.getKickParam());
                        type = 3;
                    }else {
                        break;
                    }
                    djKickUserVo.setType(type);
                    djKickUserVo.setSystem(Integer.parseInt(maintenancePlatform.getServeCode()));
                    User user = (User) SecurityUtils.getSubject().getPrincipal();
                    djKickUserVo.setOperation_by_name(user.getName());
                    djKickUserVo.setOperation_by_id(user.getId().intValue());
                    djSendService.send(djKickUserVo,maintenancePlatform.getKickUserUrl());
                    result = true;
                }catch (Exception e){
                    log.error("电竞服务={} 踢用户调用异常！参数={}", maintenancePlatform.getServerName(),JSON.toJSONString(kickUserVo), e);
                }
                break;
            default:
                result = false;
                log.error("未匹配得数据编码 {}", maintenancePlatform.getDataCode());
        }
        return result;
    }

}
