package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.panda.multiterminalinteractivecenter.dto.MaintenanceRecordDTO;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author :  ives
 * @Description :  给体育发信息处理类
 * @Date: 2022-04-10 10:51
 */
@Service
@Slf4j
public class SportSendServiceImpl {

    @Resource
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @Resource
    private MerchantManageClient merchantManageClient;


    public void checkSendStartMaintainRemindAndNoticeToSport(MaintenanceRecordVo maintenanceRecordVo) {

        try {
            String title = "维护平台电竞开始维护时发送公告至体育";
            log.info("{}, 进入方法：{}",title,maintenanceRecordVo);
            final String eSportFlag = "ty";
            if (Objects.equals(eSportFlag,maintenanceRecordVo.getDataCode()) && Objects.equals(maintenanceRecordVo.getIsSendNotice(), 1)){
                List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordVo.getDataCode());
                if (ObjectUtil.isNotEmpty(records)){
                    MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
                    BeanUtils.copyProperties(records.get(0),dto);
                    Object result = merchantManageClient.sendStartMaintainRemindAndNoticeToSport(dto);
                    log.info("维护平台体育开始维护时发送公告至体育结果：{}",result);
                }
            }
        } catch (BeansException e) {
            log.error("维护平台体育开始维护时发送公告至体育异常，原因：{}",e);
        }
    }
}
