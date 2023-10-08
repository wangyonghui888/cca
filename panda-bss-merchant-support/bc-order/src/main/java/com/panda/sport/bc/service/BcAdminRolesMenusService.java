package com.panda.sport.bc.service;

import com.panda.sport.bc.vo.BcAdminMenuVo;
import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;


import java.util.List;

/**
* @description:
* @Param:
* @return:   
* @author: YK 
* @date: 2020/9/1 14:33
*/
public interface BcAdminRolesMenusService {

    /**
    * @description: 获取角色获取对应菜单
    * @Param: [bcAdminUser]
    * @return: java.util.List<com.panda.sport.bc.vo.BcAdminMenuVo>
    * @author: YK
    * @date: 2020/9/11 12:04
    */
    List<BcAdminMenuVo> mapToGrantedMenu(BcAdminUser bcAdminUser);
}
