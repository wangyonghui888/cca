package com.panda.sport.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.admin.service.AdminUserService;
import com.panda.sport.merchant.common.po.merchant.AdminUser;
import com.panda.sport.merchant.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;

/**
 * @auth: YK
 * @Description:后台管理用户
 * @Date:2020/5/12 13:49
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

}
