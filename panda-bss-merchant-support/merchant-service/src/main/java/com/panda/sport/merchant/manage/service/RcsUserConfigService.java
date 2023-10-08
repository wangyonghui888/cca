package com.panda.sport.merchant.manage.service;


import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.RcsUserConfigDetailVO;

/**
 * @author javier
 * @date 2021/2/9
 * TODO 用户限额服务
 */
public interface RcsUserConfigService {

    /**
     * 保存操盘后台设置的用户限额数据信息
     */
    RcsUserConfigDetailVO detail(UserPO userLimit);

    UserPO getUserLimit(String userId);
}
