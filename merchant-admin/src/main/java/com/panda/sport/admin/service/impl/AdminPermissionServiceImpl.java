package com.panda.sport.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.admin.service.AdminPermissionService;
import com.panda.sport.merchant.common.po.merchant.AdminPermission;
import com.panda.sport.merchant.mapper.AdminPermissionMapper;
import org.springframework.stereotype.Service;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/12 15:50
 */
@Service
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermission> implements AdminPermissionService {
}
