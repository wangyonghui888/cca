package com.panda.sport.bc.service;

import com.panda.sport.bc.vo.BcAdminPermissionVo;

import java.util.List;

/**
 * @ClassName AdminMenuService
 * @auth YK
 * @Description 菜单栏
 * @Date 2020-09-01 14:46
 * @Version
 */
public interface BcAdminMenuService {

    /**
    * @description: 获取用户权限
    * @Param: [menuIds]
    * @return: java.util.List<com.panda.sport.bc.vo.AdminPermissionVo>
    * @author: YK
    * @date: 2020/9/1 14:49
    */
    List<BcAdminPermissionVo> getPermissionInMenuIds(List<String> menuIds);

    void allocatePermisson();
}
