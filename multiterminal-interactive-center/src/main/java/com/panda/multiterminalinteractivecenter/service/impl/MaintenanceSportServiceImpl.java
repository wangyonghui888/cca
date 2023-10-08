package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import com.panda.multiterminalinteractivecenter.feign.BackendApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.dto.ConfigPO;
import com.panda.multiterminalinteractivecenter.feign.dto.Result;
import com.panda.multiterminalinteractivecenter.mapper.MaintenanceRecordMapper;
import com.panda.multiterminalinteractivecenter.mapper.MatchInfoMapper;
import com.panda.multiterminalinteractivecenter.vo.DjKickUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-26 14:02:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
@RefreshScope
@Slf4j
public class MaintenanceSportServiceImpl {

    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @Value("${dj.sport.id:1}")
    private String djSportId;

    @Autowired
    private BackendApiClient backendApiClient;

    @Autowired
    private ESportSendServiceImpl djSendService;

    @Autowired
    private MaintenanceRecordMapper maintenanceRecordMapper;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MatchInfoMapper matchInfoMapper;

    private static final String DJ_PATH = "/v1/game/updateField";

    public void handleCloseSport(Long maintenanceRecordId){
        log.info("handleCloseSport：ID:{},进入关闭赛种方法",maintenanceRecordId);
        MaintenancePlatform maintenancePlatform = maintenancePlatformService.getById(maintenanceRecordId);
        log.info("handleCloseSport：ID:{},关闭赛种的服务为：{}",maintenanceRecordId,maintenancePlatform.getDataCode());
        switch (maintenancePlatform.getDataCode()){
            case "cp":
                break;
            case "dj":
                List<String> djList = new ArrayList<>(Arrays.asList("1", "3"));// 1客户端  3总控后台
                if (!djList.contains(maintenancePlatform.getServeCode())){
                    log.info("dj: handleCloseSport：ID:{},serverCode:{},因此无需关闭TY赛种",maintenanceRecordId,maintenancePlatform.getServeCode());
                    break;
                }
                Result<ConfigPO> result = backendApiClient.getSportConfig();
                if (result == null){
                    log.error("dj: handleCloseSport：ID:{},调用体育api接口异常！参数={}",maintenanceRecordId, JSON.toJSONString(maintenanceRecordId));
                    return;
                }
                if (!"0000000".equals(result.getCode())){
                    log.error("dj: handleCloseSport：ID:{},调用体育api接口异常！参数={} 返回消息={}", maintenanceRecordId,JSON.toJSONString(maintenanceRecordId), JSON.toJSONString(result));
                    return;
                }
                ConfigPO configPO;
                if (result.getData() != null){
                    configPO =  result.getData();
                    if (StringUtils.isEmpty(configPO.getValue())){
                        configPO.setValue(djSportId);
                    }else {
                        String[] ids = configPO.getValue().split(",");
                        List<String> filterList = new ArrayList<>(Arrays.asList(ids));
                        String[] djIds = djSportId.split(",");
                        for (String id : djIds){
                            if (!filterList.contains(id)){
                                filterList.add(id);
                            }
                        }
                        StringBuilder idStr = new StringBuilder();
                        for (String id : filterList){
                            idStr.append(id).append(",");
                        }
                        configPO.setValue(idStr.toString());
                    }
                }else {
                    configPO = new ConfigPO();
                    configPO.setName("sports");
                    configPO.setValue(djSportId);
                }
                log.info("dj: handleCloseSport：ID:{},sportsValue:{}",maintenanceRecordId,configPO.getValue());
                result =  backendApiClient.updateSportConfig(configPO);
                if (result != null && "0000000".equals(result.getCode())){
                    log.info("dj: handleCloseSport：ID:{}，DJ赛种关闭成功！",maintenanceRecordId);
                    // 修改维护状态
                    matchInfoMapper.updateByTab(TabEnum.TY.getName(), 0);
                }else{
                    log.info("dj： handleCloseSport：ID:{},DJ赛种关闭失败,调用接口返回结果：{}",maintenanceRecordId,JSON.toJSONString(result));
                }
                break;
            case "ty":
                List<String> tyList = new ArrayList<>(Arrays.asList("ty_server", "10020"));
                if (!tyList.contains(maintenancePlatform.getServeCode())){
                    log.info("ty ： handleCloseSport：ID:{},serverCode:{},因此无需关闭DJ赛种",maintenanceRecordId,maintenancePlatform.getServeCode());
                    break;
                }
                DjKickUserVo djKickUserVo = new DjKickUserVo();
                djKickUserVo.setIs_open_match(0);
                String url = DJ_PATH;
                try {
                    djSendService.send(djKickUserVo, url);
                }catch (Exception e){
                    log.error("关闭赛种消息发送失败。");
                }
                break;
            default:
        }
    }

    public void handleOpenSport(Long maintenanceRecordId){
        MaintenancePlatform maintenancePlatform = maintenancePlatformService.getById(maintenanceRecordId);
        try {
            if ("ty_server".equals(maintenancePlatform.getServeCode())){
                merchantApiClient.updateMaintainCache(System.currentTimeMillis());
            }
        }catch (Exception e){
            log.error("设置时间异常！", e);
        }
        switch (maintenancePlatform.getDataCode()){
            case "cp":
                break;
            case "dj":
                List<String> djList = new ArrayList<>(Arrays.asList("1", "3"));
                if (!djList.contains(maintenancePlatform.getServeCode())){
                    break;
                }
                List<MaintenanceRecord> list =  maintenanceRecordMapper.checkListByServerCode(djList);
                if (CollectionUtil.isNotEmpty(list)){
                    break;
                }
                Result result = backendApiClient.getSportConfig();
                if (result == null){
                    log.error("调用体育api接口异常！参数={}", JSON.toJSONString(maintenanceRecordId));
                    return;
                }
                if (!"0000000".equals(result.getCode())){
                    log.error("调用体育api接口异常！参数={} 返回消息={}", JSON.toJSONString(maintenanceRecordId), JSON.toJSONString(result));
                    return;
                }
                // 修改维护状态
                matchInfoMapper.updateByTab(TabEnum.TY.getName(), 1);
                ConfigPO configPo;
                if (result.getData() != null){
                    configPo = (ConfigPO) result.getData();
                    if (StringUtils.isEmpty(configPo.getValue())){
                        break;
                    }else {
                        String[] ids = configPo.getValue().split(",");
                        String[] djIds = djSportId.split(",");
                        List<String> filterList =new ArrayList<>(Arrays.asList(djIds));
                        StringBuilder idStr = new StringBuilder();
                        for (String id : ids){
                            if (!filterList.contains(id)){
                                idStr.append(id).append(",");
                            }
                        }
                        configPo.setValue(idStr.toString());
                    }
                }else {
                    break;
                }
                result =  backendApiClient.updateSportConfig(configPo);
                if (result != null && "0000000".equals(result.getCode())){
                    log.info("赛种打开成功！");
                }
                break;
            case "ty":
                List<String> tyList = new ArrayList<>(Arrays.asList("ty_server", "10020"));
                if (!tyList.contains(maintenancePlatform.getServeCode())){
                    break;
                }
                List<MaintenanceRecord> checkList =  maintenanceRecordMapper.checkListByServerCode(tyList);
                if (CollectionUtil.isNotEmpty(checkList)){
                    break;
                }
                DjKickUserVo djKickUserVo = new DjKickUserVo();
                djKickUserVo.setIs_open_match(1);
                try {
                    djSendService.send(djKickUserVo, DJ_PATH);
                }catch (Exception e){
                    log.error("关闭赛种消息发送失败。",e);
                }
                break;
            default:
        }
    }
}
