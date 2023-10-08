package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.DataPermissions;
import com.panda.multiterminalinteractivecenter.mapper.DataRolePermissionsMapper;
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
public class DataPermissionsServiceImpl extends ServiceImpl<DataRolePermissionsMapper, DataPermissions> implements IService<DataPermissions> {

    @Autowired
    private DataRolePermissionsMapper dataRolePermissionsMapper;

    public List<DataPermissions> findDataPermissionsByRoleId(Long roleId){
        return dataRolePermissionsMapper.findDataPermissionsByRoleId(roleId);
    }

    public APIResponse<Object> queryRoleDataList(Long roleId){
        List<DataPermissions> list = dataRolePermissionsMapper.selectAll();
        List<DataPermissions> list1 = dataRolePermissionsMapper.findDataPermissionsByRoleId(roleId);
        for (DataPermissions d : list1){
            for (DataPermissions da : list){
                if (d.getDataCode().equals(da.getDataCode())){
                    da.setStatus(1);
                    break;
                }
            }
        }
        return APIResponse.returnSuccess(list);
    }
}
