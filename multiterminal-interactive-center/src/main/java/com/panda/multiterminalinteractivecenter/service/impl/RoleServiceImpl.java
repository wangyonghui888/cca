package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.Role;
import com.panda.multiterminalinteractivecenter.mapper.RoleMapper;
import com.panda.multiterminalinteractivecenter.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-13 15:05:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IService<Role> {

    @Autowired
    private RoleMapper roleMapper;

    public List<Role> findRoleByUserId(Long userId){
       return roleMapper.queryRoleByUserId(userId);
    }

    public Role findRoleByRoleName(String roleName){
        LambdaQueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(roleName),Role::getRoleName,roleName);
        queryWrapper.orderByDesc(Role::getCreatTime);
        Role role = baseMapper.selectOne(queryWrapper);
        return  role;
    }

    public APIResponse queryRoleList(RoleVo roleVo){
        LambdaQueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(roleVo.getRoleName()),Role::getRoleName,roleVo.getRoleName());
        queryWrapper.orderByDesc(Role::getCreatTime);
        Integer page = roleVo.getPage();
        if (Objects.isNull(page)) {
            page = 1;
        }
        Integer size = roleVo.getSize();
        if (Objects.isNull(size)) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        List<Role> list = baseMapper.selectList(queryWrapper);
        PageInfo<Role> poList = new PageInfo<>(list);
        return  APIResponse.returnSuccess(poList);
    }

    public void addRole(Role role){
        role.setId(roleMapper.maxId()+1);
        baseMapper.insert(role);
    }

    public APIResponse queryUserRoleList(Long userId){
        List<Role> list = roleMapper.selectAll();
        List<Role> list1 =  roleMapper.queryRoleByUserId(userId);
        for (Role role : list1){
            for (Role role1 : list){
                if (role1.getId().equals(role.getId())){
                    role1.setStatus(1);
                    break;
                }
            }
        }
        return  APIResponse.returnSuccess(list);
    }
}
