package com.panda.sport.bc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bc.service.BcAdminRoleService;
import com.panda.sport.bc.mapper.BcAdminRoleMapper;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRole;
import org.springframework.stereotype.Service;

/**
 * @ClassName BcAdminRoleServiceImpl
 * @auth YK
 * @Description 用户角色
 * @Date 2020-09-03 13:55
 * @Version
 */
@Service
public class BcAdminRoleServiceImpl extends ServiceImpl<BcAdminRoleMapper, BcAdminRole> implements BcAdminRoleService {
}
