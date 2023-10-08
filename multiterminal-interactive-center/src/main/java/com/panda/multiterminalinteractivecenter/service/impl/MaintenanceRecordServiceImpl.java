package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.dto.RecordListDTO;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.mapper.MaintenanceRecordMapper;
import com.panda.multiterminalinteractivecenter.utils.DateUtils;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MaintenanceRecordServiceImpl extends ServiceImpl<MaintenanceRecordMapper, MaintenanceRecord> implements IService<MaintenanceRecord> {

    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    public void save(MaintenanceRecordVo maintenanceRecordVo){
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        BeanUtils.copyProperties(maintenanceRecordVo,maintenanceRecord);
        Long nowTime = System.currentTimeMillis();
        maintenanceRecord.setCreateTime(nowTime);
        maintenanceRecord.setUpdateTime(nowTime);
        maintenanceRecord.setRealStartTime(nowTime);
        maintenanceRecord.setPlanEndTime(maintenanceRecordVo.getMaintenanceEndTime());
        super.save(maintenanceRecord);
    }

    public List<MaintenanceRecord> getByPlatformIds(List<Long> platformIds){
        LambdaQueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<MaintenanceRecord>().lambda();
        queryWrapper.in(MaintenanceRecord::getMaintenancePlatformId,platformIds);
        return baseMapper.selectList(queryWrapper);
    }

    public MaintenanceRecord getByPlatformId(Long platformId){
        LambdaQueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<MaintenanceRecord>().lambda();
        queryWrapper.eq(MaintenanceRecord::getMaintenancePlatformId,platformId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 获取到已关闭的记录
     * @return
     */
    public List<MaintenanceRecord> getNeedResetRecord(){
        LambdaQueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<MaintenanceRecord>().lambda();
        queryWrapper.eq(MaintenanceRecord::getMaintenanceStatus, Constant.MaintenanceStatus.THEEND.getValue());
        Date endTime = DateUtils.addDateHours(new Date(),-2);
        queryWrapper.le(MaintenanceRecord::getMaintenanceEndTime,endTime.getTime());
        return baseMapper.selectList(queryWrapper);
    }

    public RecordListDTO findServerStatus(){
        RecordListDTO dto = new RecordListDTO();
        List<MaintenancePlatform> list = maintenancePlatformService.getTyServerList();
        if (CollectionUtils.isEmpty(list)){
            return dto;
        }
        List<Long> ids = list.stream().map(MaintenancePlatform::getId).collect(Collectors.toList());
        LambdaQueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<MaintenanceRecord>().lambda();
        queryWrapper.in(MaintenanceRecord::getMaintenancePlatformId ,ids);
        List<MaintenanceRecord> records =  baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(records)){
            return dto;
        }
        for (MaintenanceRecord record : records){
            if (record.getMaintenanceStatus() == 2){
                dto.setStatus(1);
                break;
            }
        }
        return dto;
    }
}
