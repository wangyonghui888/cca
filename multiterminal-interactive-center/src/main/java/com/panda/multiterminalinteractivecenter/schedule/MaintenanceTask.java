package com.panda.multiterminalinteractivecenter.schedule;

import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.mapper.MaintenanceRecordMapper;
import com.panda.multiterminalinteractivecenter.service.impl.*;
import com.panda.multiterminalinteractivecenter.utils.DateUtils;
import com.panda.multiterminalinteractivecenter.vo.KickUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lary
 * @version 1.0.0
 * @ClassName MaintenanceTask.java
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.schedule
 * @createTime 2022/03/28
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceTask {

    private final MaintenanceRecordServiceImpl maintenanceRecordService;

    private final MaintenanceRecordMapper maintenanceRecordMapper;

    private final KickUserServiceImpl kickUserService;

    private final MaintenanceSportServiceImpl maintenanceSportService;

    private final MaintenanceLogServiceImpl maintenanceLogService;

    private final MerchantApiClient merchantApiClient;

    /**
     * 移除已关闭的记录
     */
    @Scheduled(cron = "* 0/5 * * * ?")
    public void resetMaintenanceRecord() {
        //log.info("定时处理到时间或已关闭的记录");
        try {
            List<MaintenanceRecord> records = maintenanceRecordService.getNeedResetRecord();
            records.forEach(maintenanceRecordService::removeById);
        } catch (Exception e) {
            log.error("定时处理到时间或已关闭的记录", e);
        }

    }


    /**
     * 提前5分钟调用关闭体育赛种，防止2分钟未扫描到
     */
    @Scheduled(cron = "* 0/2 * * * ?")
    public void handleCloseSportTask() {
        long starTime = System.currentTimeMillis() + 5 * 60 * 1_000;
        List<MaintenanceRecord> list = maintenanceRecordMapper.getStarMaintenanceRecord(starTime);
        for (MaintenanceRecord record : list) {
            log.info("{}检测到ID为{},维护还是时间：{},关闭体育赛中任务开始", DateUtils.format(starTime, DateUtils.DATE_TIME_PATTERN), record.getId(),DateUtils.format(record.getMaintenanceStartTime(), DateUtils.DATE_TIME_PATTERN));
            try {
                maintenanceSportService.handleCloseSport(record.getMaintenancePlatformId());
            } catch (Exception e) {
                log.error("调用关闭球种异常！", e);
            }
        }
    }

    /**
     * 定时将待维护变更为维护中
     */
    @Scheduled(cron = "* 0/2 * * * ?")
    public void starMaintenanceRecord() {
        long starTime = System.currentTimeMillis();
        //log.info("定时处理待维护记录 {}", starTime);
        try {
            List<MaintenanceRecord> listRemind = maintenanceRecordMapper.getStarMaintenanceRecordIsRemind(starTime);
            for (MaintenanceRecord record : listRemind) {
                sendUserMsg(record);
            }
            List<MaintenanceRecord> list = maintenanceRecordMapper.getStarMaintenanceRecord(starTime);
            for (MaintenanceRecord record : list) {
                maintenanceLogService.saveMaintenanceLogVo(record.getMaintenancePlatformId(), "system", Constant.OperationType.STARTMAINTENANCE.getValue(), "开始维护", "");
                if (record.getIsKickUser() == 1) {
                    KickUserVo userVo = new KickUserVo();
                    userVo.setSystemId(record.getMaintenancePlatformId());
                    userVo.setKickType(4);
/*                    try {
                        kickUserService.kickUser(userVo, "");
                    } catch (Exception e) {
                        log.error("处理失败 {}", JSON.toJSONString(userVo));
                    }*/
                    try {
                        maintenanceSportService.handleCloseSport(record.getMaintenancePlatformId());
                    } catch (Exception e) {
                        log.error("调用关闭球种异常！", e);
                    }
                }
            }
            maintenanceRecordMapper.starMaintenancePlatform(starTime);
        } catch (Exception e) {
            log.error("定时处理待维护记录", e);
        }
    }

    /**
     * 发送提醒给客户端用户
     *
     * @param maintenanceRecord 待维护记录
     */
    private void sendUserMsg(MaintenanceRecord maintenanceRecord) {
        if (maintenanceRecord != null) {
            if (maintenanceRecord.getMaintenanceStartTime() != null) {
                merchantApiClient.updateMaintainCache(maintenanceRecord.getMaintenanceStartTime());
            }
            maintenanceRecordMapper.updateStarMaintenanceRecordIsRemind(maintenanceRecord.getId());
        }
    }

}
