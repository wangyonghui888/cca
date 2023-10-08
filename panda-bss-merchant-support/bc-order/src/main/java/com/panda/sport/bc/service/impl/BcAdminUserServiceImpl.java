package com.panda.sport.bc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bc.service.BcAdminUserService;

import com.panda.sport.bc.mapper.BcAdminUserMapper;
import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;
import org.springframework.stereotype.Service;


/**
 * @ClassName AdminUserServiceImpl
 * @auth YK
 * @Description 后台管理用户
 * @Date 2020-09-01 12:12
 * @Version
 */
@Service
public class BcAdminUserServiceImpl extends ServiceImpl<BcAdminUserMapper, BcAdminUser> implements BcAdminUserService {

}
