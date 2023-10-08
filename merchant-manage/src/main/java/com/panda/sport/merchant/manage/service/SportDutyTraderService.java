package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.bss.SportDutyTraderPO;

import java.util.List;

/**
 * @author javier
 * @date 2021/2/11
 * 操盘手排班服务
 */
public interface SportDutyTraderService {

    /**
     * 同步操盘手信息
     * @param list 操盘手集合
     */
    void syncTrader(List<SportDutyTraderPO> list);
}
