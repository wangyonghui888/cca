package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.AdminRolesPermissions;
import org.springframework.stereotype.Repository;

/**
 * 用户权限分配
 */
@Repository
public interface AdminRolesPermissionsMapper extends BaseMapper<AdminRolesPermissions> {
}
