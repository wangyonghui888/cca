package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceLog;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.mapper.MaintenanceLogMapper;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceLogPageVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class MaintenanceLogServiceImpl extends ServiceImpl<MaintenanceLogMapper, MaintenanceLog> implements IService<MaintenanceLog> {

    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    public APIResponse<?> pageList(MaintenanceLogPageVo vo){
        LambdaQueryWrapper<MaintenanceLog> queryWrapper = new QueryWrapper<MaintenanceLog>().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(vo.getDataCode()),MaintenanceLog::getDataCode,vo.getDataCode());
        queryWrapper.eq(vo.getOperationType()>0,MaintenanceLog::getOperationType,vo.getOperationType());
        queryWrapper.eq(StringUtils.isNotBlank(vo.getServerName()),MaintenanceLog::getServerName,vo.getServerName());
        queryWrapper.eq(StringUtils.isNotBlank(vo.getOperators()),MaintenanceLog::getOperators,vo.getOperators());
        queryWrapper.ge(!Objects.isNull(vo.getStartTime()),MaintenanceLog::getCreateTime,vo.getStartTime());
        queryWrapper.le(!Objects.isNull(vo.getEndTime()),MaintenanceLog::getCreateTime,vo.getEndTime());
        queryWrapper.orderByDesc(MaintenanceLog::getCreateTime);
        Integer page = vo.getPage();
        if (Objects.isNull(page)) {
            page = 1;
        }
        Integer size = vo.getSize();
        if (Objects.isNull(size)) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        List<MaintenanceLog> list = baseMapper.selectList(queryWrapper);
        list.forEach(maintenanceLog -> maintenanceLog.setDataCodeName(Constant.DataCodeType.getValueByKey(maintenanceLog.getDataCode())));
        PageInfo<MaintenanceLog> poList = new PageInfo<>(list);
        return  APIResponse.returnSuccess(poList);
    }

    public void save(MaintenanceLogVo maintenanceLogVo){
        MaintenanceLog maintenanceLog = new MaintenanceLog();
        BeanUtils.copyProperties(maintenanceLogVo,maintenanceLog);
        maintenanceLog.setCreateTime(new Date().getTime());
        super.save(maintenanceLog);
    }

    public void saveMaintenanceLogVo(Long platformId, String username, int operationType, String operationContent, String ipAddr){

        MaintenancePlatform maintenancePlatform = maintenancePlatformService.getById(platformId);

        MaintenanceLogVo maintenanceLogVo = new MaintenanceLogVo();
        maintenanceLogVo.setDataCode(maintenancePlatform.getDataCode());
        maintenanceLogVo.setOperationType(operationType);
        maintenanceLogVo.setOperators(username);
        maintenanceLogVo.setServerName(maintenancePlatform.getServerName());
        maintenanceLogVo.setOperationContent(maintenancePlatform.getServerName()+","+operationContent);
        maintenanceLogVo.setOperationIp(ipAddr);
        save(maintenanceLogVo);
    }
}
