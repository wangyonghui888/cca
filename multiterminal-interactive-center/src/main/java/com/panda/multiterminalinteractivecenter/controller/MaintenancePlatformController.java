package com.panda.multiterminalinteractivecenter.controller;

import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.service.impl.*;
import com.panda.multiterminalinteractivecenter.utils.DateUtils;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.KickUserVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceLogVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordEditVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author :  lary
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.controller
 * @Description :  TODO
 * @Date: 2022-03-19 13:08:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/maintenancePatform")
public class MaintenancePlatformController {
    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;
    @Autowired
    private MaintenanceLogServiceImpl maintenanceLogService;

    @Autowired
    private MaintenanceSportServiceImpl maintenanceSportService;

    @Autowired
    private KickUserServiceImpl kickUserService;

    @Resource
    private ESportSendServiceImpl eSportSendService;

    @Resource
    private SportSendServiceImpl sportsSendService;
    /**
     * 首页
     * @return
     */
    @PostMapping("/getHomeDatas")
    public APIResponse<?> getHomeDatas(@RequestBody KickUserVo kickUserVo) {
        return APIResponse.returnSuccess(maintenancePlatformService.getHomeDatas(kickUserVo));
    }

    /**
     * 设置维护记录
     * @param maintenanceRecordVo
     * @return
     */
    @Transactional
    @PostMapping("/setMaintainRecord")
    public APIResponse setMaintainRecord(HttpServletRequest request, @Validated @RequestBody MaintenanceRecordVo maintenanceRecordVo) {
        String token = request.getHeader("token");
        String username = JWTUtil.getUsername(token);
        if (StringUtils.isBlank(maintenanceRecordVo.getDataCode())){
            return APIResponse.returnFail("所属项目不为空");
        }
        List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordVo.getDataCode());
        if (!Objects.isNull(records)&&records.size()>0&&validateMaintenanceRecords(records)){
            return APIResponse.returnFail("维护详情有未结束的记录");
        }
        String ipAddr = IPUtils.getIpAddr(request);
        List<MaintenanceRecord> maintenanceRecords = maintenanceRecordService.getByPlatformIds(maintenanceRecordVo.getMaintenancePlatformIds());
        //删除已结束的记录
        maintenanceRecords.forEach(maintenanceRecord -> {
            if (maintenanceRecord.getMaintenanceStatus().intValue() == Constant.MaintenanceStatus.THEEND.getValue()){
                maintenanceRecordService.removeById(maintenanceRecord);
            }
        });
        maintenanceRecordVo.setMaintenanceStatus(Constant.MaintenanceStatus.NOSTARTING.getValue());
        AtomicReference<String> serverName = new AtomicReference<>("");
        AtomicReference<String> dataCode = new AtomicReference<>("");
        maintenanceRecordVo.getMaintenancePlatformIds().forEach(platformId ->{
            maintenanceRecordVo.setMaintenancePlatformId(platformId);
            maintenanceRecordService.save(maintenanceRecordVo);
            MaintenancePlatform maintenancePlatform = maintenancePlatformService.getById(platformId);
            dataCode.set(maintenancePlatform.getDataCode());
            serverName.set(serverName + " " + maintenancePlatform.getServerName());
            if (maintenanceRecordVo.getIsSendNotice()==1){
                maintenanceLogService.saveMaintenanceLogVo(platformId,username,Constant.OperationType.SENDNOTICE.getValue(),"发送维护公告",ipAddr);
            }
            if (maintenanceRecordVo.getIsRemind() == 1){
                maintenanceLogService.saveMaintenanceLogVo(platformId,username,Constant.OperationType.ISREMIND.getValue(),"弹出用户提醒",ipAddr);
            }
        });
        MaintenanceLogVo maintenanceLogVo = new MaintenanceLogVo();
        maintenanceLogVo.setDataCode(dataCode.get());
        maintenanceLogVo.setOperationType(Constant.OperationType.SETMAINTENANCE.getValue());
        maintenanceLogVo.setOperators(username);
        maintenanceLogVo.setServerName(serverName.get());
        maintenanceLogVo.setOperationContent(serverName.get()+",维护开始时间:"+ DateUtils.format(maintenanceRecordVo.getMaintenanceStartTime(),DateUtils.DATE_TIME_PATTERN));
        maintenanceLogVo.setOperationIp(ipAddr);
        maintenanceLogService.save(maintenanceLogVo);
        // dj
        eSportSendService.checkSendStartMaintainRemindAndNoticeToESport(maintenanceRecordVo);
        // ty
        sportsSendService.checkSendStartMaintainRemindAndNoticeToSport(maintenanceRecordVo);
        // cp
        eSportSendService.checkSendStartMaintainRemindAndNoticeToLottery(maintenanceRecordVo);
        return APIResponse.returnSuccess();
    }
    private boolean validateMaintenanceRecords(List<MaintenanceRecord> maintenanceRecords){
        boolean bol = maintenanceRecords.stream().filter(maintenanceRecord ->
                maintenanceRecord.getMaintenanceStatus().intValue() == Constant.MaintenanceStatus.NOSTARTING.getValue()||
                        maintenanceRecord.getMaintenanceStatus().intValue() == Constant.MaintenanceStatus.STARTING.getValue()).findAny().isPresent();
        return bol;
    }
    /**
     * 开始维护
     * @param request
     * @param maintenanceRecordEditVo
     * @return
     */
    @Transactional
    @PostMapping("/startMaintainRecord")
    public APIResponse startMaintainRecord(HttpServletRequest request, @RequestBody MaintenanceRecordEditVo maintenanceRecordEditVo){
        String token = request.getHeader("token");
        String username = JWTUtil.getUsername(token);
        String ipAddr = IPUtils.getIpAddr(request);
        if (!Objects.isNull(maintenanceRecordEditVo.getPlatformId())){
            MaintenanceRecord record = maintenanceRecordService.getByPlatformId(maintenanceRecordEditVo.getPlatformId());
            if (Objects.isNull(record)){
                if (StringUtils.isBlank(maintenanceRecordEditVo.getDataCode())){
                    return APIResponse.returnFail("所属项目不为空");
                }
                List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordEditVo.getDataCode());
                if (Objects.isNull(records)|| records.size()==0){
                    return APIResponse.returnFail("未设置维护，请先设置维护");
                }
                //未设置维护的，单个直接开始维护
                MaintenanceRecordVo maintenanceRecordVo = new MaintenanceRecordVo();
                BeanUtils.copyProperties(records.get(0),maintenanceRecordVo);
                maintenanceRecordVo.setMaintenanceStatus(Constant.MaintenanceStatus.STARTING.getValue());
                maintenanceRecordVo.setMaintenancePlatformId(maintenanceRecordEditVo.getPlatformId());
                maintenanceRecordVo.setRealStartTime(System.currentTimeMillis());
                maintenanceRecordService.save(maintenanceRecordVo);
                maintenanceLogService.saveMaintenanceLogVo(maintenanceRecordEditVo.getPlatformId(),username,Constant.OperationType.STARTMAINTENANCE.getValue(),"开始维护",ipAddr);
                maintenanceSportService.handleCloseSport(maintenanceRecordEditVo.getPlatformId());
            }else {
                //已设置维护的，单个开始维护
                startMaintenance(record,username,ipAddr);
            }
        }else {
            List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordEditVo.getDataCode());
            //已设置维护的，批量开始维护的
            if (!Objects.isNull(records)&& records.size()>0){
                records.forEach(maintenanceRecord -> {
                    if (maintenanceRecord.getMaintenanceStatus().intValue() == Constant.MaintenanceStatus.NOSTARTING.getValue()){
                        startMaintenance(maintenanceRecord,username,ipAddr);
                    }
                });
            }
        }

        return APIResponse.returnSuccess();
    }
    //开始维护
    private void startMaintenance(MaintenanceRecord maintenanceRecord, String username,String ipAddr){
        maintenanceRecord.setMaintenanceStatus(Constant.MaintenanceStatus.STARTING.getValue());
        maintenanceRecord.setRealStartTime(System.currentTimeMillis());
        maintenanceRecord.setUpdateTime(System.currentTimeMillis());
        maintenanceRecordService.updateById(maintenanceRecord);
        maintenanceSportService.handleCloseSport(maintenanceRecord.getMaintenancePlatformId());
        if (maintenanceRecord.getIsKickUser() == 1) {
            KickUserVo userVo = new KickUserVo();
            userVo.setSystemId(maintenanceRecord.getMaintenancePlatformId());
            userVo.setKickType(4);
            try {
                kickUserService.kickUser(userVo,ipAddr);
            } catch (Exception e) {
                log.error("处理失败 {}", JSON.toJSONString(userVo));
            }

            //保存日志
            maintenanceLogService.saveMaintenanceLogVo(maintenanceRecord.getMaintenancePlatformId(),username,Constant.OperationType.KICKUSER.getValue(),"踢用户",ipAddr);
        }else {
            // 保存日志
            maintenanceLogService.saveMaintenanceLogVo(maintenanceRecord.getMaintenancePlatformId(),username,Constant.OperationType.STARTMAINTENANCE.getValue(),"开始维护",ipAddr);
        }
    }

    /**
     * 结束维护
     * @param request
     * @param maintenanceRecordEditVo
     * @return
     */
    @Transactional
    @PostMapping("/endMaintainRecord")
    public APIResponse endMaintainRecord(HttpServletRequest request, @RequestBody MaintenanceRecordEditVo maintenanceRecordEditVo){
        String token = request.getHeader("token");
        String username = JWTUtil.getUsername(token);
        String ipAddr = IPUtils.getIpAddr(request);
        if (StringUtils.isNoneBlank(maintenanceRecordEditVo.getDataCode())){
            List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordEditVo.getDataCode());
            if (!Objects.isNull(records)&& records.size()>0){
                records.forEach(maintenanceRecord -> {
                    if (maintenanceRecord.getMaintenanceStatus().intValue() == Constant.MaintenanceStatus.STARTING.getValue()){
                        endMaintenance(maintenanceRecord,username,ipAddr);
                    }
                });
            }
        }else {
            MaintenanceRecord maintenanceRecord = maintenanceRecordService.getById(maintenanceRecordEditVo.getRecordId());
            if (Objects.isNull(maintenanceRecord)){
                return APIResponse.returnFail("未设置维护，请先设置维护");
            }
            endMaintenance(maintenanceRecord,username,ipAddr);
        }
        eSportSendService.checkSendEndMaintainRemindAndNoticeToESport(maintenanceRecordEditVo);
        eSportSendService.checkSendStartMaintainRemindAndNoticeToLottery(maintenanceRecordEditVo);
        return APIResponse.returnSuccess();
    }

    private void endMaintenance(MaintenanceRecord maintenanceRecord, String username,String ipAddr){
        maintenanceRecord.setMaintenanceStatus(Constant.MaintenanceStatus.THEEND.getValue());
        maintenanceRecord.setMaintenanceEndTime(System.currentTimeMillis());
        maintenanceRecord.setUpdateTime(System.currentTimeMillis());
        maintenanceRecordService.updateById(maintenanceRecord);
        maintenanceSportService.handleOpenSport(maintenanceRecord.getMaintenancePlatformId());
        //保存日志
        maintenanceLogService.saveMaintenanceLogVo(maintenanceRecord.getMaintenancePlatformId(),username,Constant.OperationType.ENDMAINTENANCE.getValue(),"结束维护",ipAddr);
    }
    /**
     * 延长30分钟维护
     * @param request
     * @param maintenanceRecordEditVo
     * @return
     */
    @Transactional
    @PostMapping("/add30minute")
    public APIResponse addTimes(HttpServletRequest request, @RequestBody MaintenanceRecordEditVo maintenanceRecordEditVo){
        String token = request.getHeader("token");
        String username = JWTUtil.getUsername(token);
        String ipAddr = IPUtils.getIpAddr(request);
        MaintenanceRecord record = null;
        if (StringUtils.isNoneBlank(maintenanceRecordEditVo.getDataCode())){
            List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordEditVo.getDataCode());
            if (!Objects.isNull(records)&& records.size()>0){
                records.forEach(maintenanceRecord -> {
                    if (maintenanceRecord.getMaintenanceStatus().intValue() == Constant.MaintenanceStatus.STARTING.getValue()){
                        add30Minute(maintenanceRecord,username,ipAddr);
                    }
                });
                record = records.get(0);
            }
        }else {
            MaintenanceRecord maintenanceRecord = maintenanceRecordService.getById(maintenanceRecordEditVo.getRecordId());
            if (Objects.isNull(maintenanceRecord)){
                return APIResponse.returnFail("未设置维护，请先设置维护");
            }
            add30Minute(maintenanceRecord,username,ipAddr);
            record = maintenanceRecord;
        }
        return APIResponse.returnSuccess(record);
    }

    private void add30Minute(MaintenanceRecord maintenanceRecord,String username,String ipAddr){
        Date endTime = new Date(maintenanceRecord.getMaintenanceEndTime());
        endTime = DateUtils.addDateMinutes(endTime,30);
        maintenanceRecord.setMaintenanceEndTime(endTime.getTime());
        Integer superimposeTime = maintenanceRecord.getSuperimposeTime().intValue() +30;
        maintenanceRecord.setSuperimposeTime(superimposeTime);
        maintenanceRecord.setUpdateTime(System.currentTimeMillis());
        maintenanceRecordService.updateById(maintenanceRecord);
        //保存日志
        maintenanceLogService.saveMaintenanceLogVo(maintenanceRecord.getMaintenancePlatformId(),username,Constant.OperationType.ADD30MINUTE.getValue(),"延长"+30+"分钟维护",ipAddr);
    }
}
