package com.panda.multiterminalinteractivecenter.service.impl;

import com.panda.multiterminalinteractivecenter.entity.Permissions;
import com.panda.multiterminalinteractivecenter.mapper.PermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-14 14:35:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
public class PermissionsServiceImpl {

    @Autowired
    private PermissionsMapper permissionsMapper;

    public List<Permissions> findPermissionsByRoleId(Long roleId){
        return permissionsMapper.findPermissionsByRoleId(roleId);
    }
}
