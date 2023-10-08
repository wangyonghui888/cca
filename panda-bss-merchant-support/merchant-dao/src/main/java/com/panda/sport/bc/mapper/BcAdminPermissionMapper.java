package com.panda.sport.bc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bcorder.BcAdminPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @description:   
* @Param:
* @return:   
* @author: YK 
* @date: 2020/9/1 14:27
*/
@Repository
public interface BcAdminPermissionMapper extends BaseMapper<BcAdminPermission> {

    List<BcAdminPermission> getPermissionInMenuIds(@Param("menuIds") List<String> menuIds);
}
