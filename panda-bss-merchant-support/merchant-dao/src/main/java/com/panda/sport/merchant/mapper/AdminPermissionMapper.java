package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.AdminPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限名称
 */
@Repository
public interface AdminPermissionMapper extends BaseMapper<AdminPermission> {


    List<AdminPermission> getPermissionInMenuIds(@Param("menuIds") List<String> menuIds);


    List<AdminPermission> getPermission();
}
