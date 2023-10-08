package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.KickUserLog;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.mapper.KickUserLogMapper;
import com.panda.multiterminalinteractivecenter.vo.KickUserLogPageVo;
import com.panda.multiterminalinteractivecenter.vo.KickUserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-20 15:01:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
public class KickUserLogServiceImpl extends ServiceImpl<KickUserLogMapper, KickUserLog> implements IService<KickUserLog> {

    @Autowired
    private KickUserLogMapper kickUserLogMapper;

    public void saveLog(String systemName, Integer operationType,String operationIp, KickUserVo kickUserVo, String dataCode){
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            if (user == null){
                return;
            }
            KickUserLog log = new KickUserLog();
            log.setCreateTime(System.currentTimeMillis());
            log.setDataCode(dataCode);
            log.setServerName(systemName);
            StringBuilder param = new StringBuilder();
            if (StringUtils.isNotEmpty(kickUserVo.getMerchantCode())){
                param.append("[商户编码=").append(kickUserVo.getMerchantCode()).append("]");
            }
            if (StringUtils.isNotEmpty(kickUserVo.getKickParam())){
                param.append("[用户id=").append(kickUserVo.getKickParam()).append("]");
            }
            log.setOperationContent(param.toString());
            log.setOperationType(operationType);
            log.setOperators(user.getName());
            log.setOperationIp(operationIp);
            kickUserLogMapper.insert(log);
        }catch (Exception e){
            log.error("获取用户异常！");
        }

    }

    public APIResponse<?> pageList(KickUserLogPageVo vo){
        LambdaQueryWrapper<KickUserLog> queryWrapper = new QueryWrapper<KickUserLog>().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(vo.getDataCode()),KickUserLog::getDataCode,vo.getDataCode());
        queryWrapper.eq(vo.getOperationType()>0,KickUserLog::getOperationType,vo.getOperationType());
        queryWrapper.eq(StringUtils.isNotBlank(vo.getServerName()),KickUserLog::getServerName,vo.getServerName());
        queryWrapper.eq(StringUtils.isNotBlank(vo.getOperators()),KickUserLog::getOperators,vo.getOperators());
        queryWrapper.ge(!Objects.isNull(vo.getStartTime()),KickUserLog::getCreateTime,vo.getStartTime());
        queryWrapper.le(!Objects.isNull(vo.getEndTime()),KickUserLog::getCreateTime,vo.getEndTime());
        queryWrapper.orderByDesc(KickUserLog::getCreateTime);
        Integer page = vo.getPage();
        if (Objects.isNull(page)) {
            page = 1;
        }
        Integer size = vo.getSize();
        if (Objects.isNull(size)) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        List<KickUserLog> list = baseMapper.selectList(queryWrapper);
        PageInfo<KickUserLog> poList = new PageInfo<>(list);
        return  APIResponse.returnSuccess(poList);
    }

}
