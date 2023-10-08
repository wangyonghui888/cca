package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.entity.UserRoles;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.mapper.UserRolesMapper;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.UserVo;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-11 15:10:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRolesMapper, UserRoles> implements IService<UserRoles> {

    @Autowired
    private MerchantLogService merchantLogService;

    public void addUserRoles(UserVo userVo, HttpServletRequest request){
        LambdaQueryWrapper<UserRoles> queryWrapper = new QueryWrapper<UserRoles>().lambda();
        queryWrapper.eq(userVo != null,UserRoles::getUserId,userVo.getId());
        List<UserRoles> roles = baseMapper.selectList(queryWrapper);
        baseMapper.delete(queryWrapper);
        if (CollectionUtil.isNotEmpty(userVo.getRoleIds())){
            for (Long id : userVo.getRoleIds()){
                UserRoles userRoles = new UserRoles();
                userRoles.setUserId(userVo.getId());
                userRoles.setRoleId(id);
                baseMapper.insert(userRoles);
            }
        }
        String beforeValues = CollectionUtil.isNotEmpty(roles) ? JSON.toJSONString(roles.stream().map(UserRoles::getRoleId).collect(Collectors.toList())) : "-";
        String afterValues = CollectionUtil.isNotEmpty(userVo.getRoleIds()) ? JSON.toJSONString(userVo.getRoleIds()) : "-";
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("addRoles"), beforeValues, afterValues);
        merchantLogService.saveLog(MerchantLogPageEnum.ROLE_MANAGE, MerchantLogTypeEnum.ADD_ROLE, filedVO,  null, String.valueOf(userVo.getId()), request);
    }

}
