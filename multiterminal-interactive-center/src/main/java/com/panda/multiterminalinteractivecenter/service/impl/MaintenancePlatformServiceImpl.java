package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.dto.RecordListDTO;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import com.panda.multiterminalinteractivecenter.feign.BackendApiClient;
import com.panda.multiterminalinteractivecenter.mapper.MaintenancePlatformMapper;
import com.panda.multiterminalinteractivecenter.mapper.MatchInfoMapper;
import com.panda.multiterminalinteractivecenter.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-19 14:11:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
public class MaintenancePlatformServiceImpl extends ServiceImpl<MaintenancePlatformMapper, MaintenancePlatform> implements IService<MaintenancePlatform> {

    @Autowired
    private MaintenancePlatformMapper maintenancePlatformMapper;
    @Autowired
    private MaintenanceRecordServiceImpl maintenanceRecordService;
    @Autowired
    private MatchInfoMapper matchInfoMapper;
    @Autowired
    private BackendApiClient backendApiClient;

    public List<ServeVo> getServes(KickUserVo kickUserVo){
        if (CollectionUtils.isEmpty(kickUserVo.getDataCodes())) {
            return null;
        }
        List<MaintenancePlatform> list = maintenancePlatformMapper.findListByDataCode(kickUserVo.getDataCodes());
        Map<String, List<MaintenancePlatform>> listMap = list.stream().collect(Collectors.groupingBy(MaintenancePlatform::getDataCode));
        List<ServeVo> result = new ArrayList<>(listMap.size());
        for (Map.Entry<String, List<MaintenancePlatform>> entry : listMap.entrySet()) {
            ServeVo serveVo = new ServeVo();
            serveVo.setDataCode(entry.getKey());
            serveVo.setServes(entry.getValue());
            result.add(serveVo);
        }
        return result;
    }

    public List<HomeVo> getHomeDatas(KickUserVo kickUserVo){
        if (CollectionUtils.isEmpty(kickUserVo.getDataCodes())) {
            return null;
        }
        List<MaintenancePlatform> list = maintenancePlatformMapper.findListByDataCode(kickUserVo.getDataCodes());
        Map<String, List<MaintenancePlatform>> listMap = list.stream().collect(Collectors.groupingBy(MaintenancePlatform::getDataCode));
        List<HomeVo> result = new ArrayList<>(listMap.size());
        for (Map.Entry<String, List<MaintenancePlatform>> entry : listMap.entrySet()) {
            HomeVo  homeVo= new HomeVo();
            homeVo.setDataCode(entry.getKey());
            if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(entry.getKey(), TabEnum.TY.getName())
            || org.apache.commons.lang3.StringUtils.equalsIgnoreCase(entry.getKey(), TabEnum.DJ.getName())){
                // 单独处理球种开关信息
                List<MatchInfo> matchInfoList = matchInfoMapper.selectAllList();
                homeVo.setMatchInfo(matchInfoList);
            }
            List<MaintenancePlatform> servers = entry.getValue();
            List<MaintenanceRecord> records = Lists.newArrayList();
            servers.forEach(maintenancePlatform -> {
                MaintenanceRecord record = maintenanceRecordService.getByPlatformId(maintenancePlatform.getId());
                MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
                if (!Objects.isNull(record)){
                    BeanUtils.copyProperties(record,maintenanceRecord);
                }else {
                    maintenanceRecord.setMaintenancePlatformId(maintenancePlatform.getId());
                }
                maintenanceRecord.setServerName(maintenancePlatform.getServerName());
                maintenanceRecord.setKickUserType(maintenancePlatform.getKickUserType());
                records.add(maintenanceRecord);
            });
            List<MaintenanceRecord> sortByTimeRecords = records.stream().sorted(Comparator.comparing(MaintenanceRecord::getCreateTime, Comparator.reverseOrder())).collect(Collectors.toList());
            List<MaintenanceRecord> sortByStatusRecords = sortRecords(sortByTimeRecords,Constant.MaintenanceStatus.THEEND.getValue());
            sortByStatusRecords = sortRecords(sortByStatusRecords,Constant.MaintenanceStatus.NOSTARTING.getValue());
            sortByStatusRecords = sortRecords(sortByStatusRecords,Constant.MaintenanceStatus.STARTING.getValue());
            homeVo.setRecords(sortByStatusRecords);
            result.add(homeVo);
        }
        return result;
    }

    private List<MatchInfo> getMatchInfo(String tab) {
        return matchInfoMapper.selectListByTab(tab);
    }

    private List<MaintenanceRecord> sortRecords(List<MaintenanceRecord> records,int status){
        List<MaintenanceRecord> newRecords = Lists.newArrayList();
        Iterator it = records.iterator();
        while(it.hasNext()){
            MaintenanceRecord record = (MaintenanceRecord) it.next();
            if(!Objects.isNull(record) && !Objects.isNull(record.getMaintenanceStatus())
                    &&record.getMaintenanceStatus().intValue() == status){
                newRecords.add(record);
                it.remove();
            }
        }
        newRecords.addAll(records);
        return newRecords;
    }

    public List<MaintenancePlatform> getTyServerList(){
        RecordListDTO dto = new RecordListDTO();
        List<String> tyList = new ArrayList<>(Arrays.asList("ty_server", "10020"));
        LambdaQueryWrapper<MaintenancePlatform> queryWrapper = new QueryWrapper<MaintenancePlatform>().lambda();
        queryWrapper.in(MaintenancePlatform::getServeCode ,tyList);
        return baseMapper.selectList(queryWrapper);
    }

    public List<MaintenanceRecord> getListByDataCode(String dataCode){
        List<MaintenanceRecord> records = Lists.newArrayList();
        LambdaQueryWrapper<MaintenancePlatform> queryWrapper = new QueryWrapper<MaintenancePlatform>().lambda();
        queryWrapper.eq(MaintenancePlatform::getDataCode,dataCode);
        List<MaintenancePlatform> platforms = baseMapper.selectList(queryWrapper);
        platforms.forEach(maintenancePlatform -> {
            MaintenanceRecord record = maintenanceRecordService.getByPlatformId(maintenancePlatform.getId());
            if (!Objects.isNull(record)){
                records.add(record);
            }
        });
        return records;
    }

    public APIResponse querySystemInfo(String serverCode){
        if (StringUtils.isEmpty(serverCode)){
            return  APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        String dataCode = serverCode.toLowerCase();
        MaintenanceRecordOutDto maintenanceRecordOutDto = maintenancePlatformMapper.querySystemInfo(dataCode);
        return APIResponse.returnSuccess(maintenanceRecordOutDto);
    }
}
