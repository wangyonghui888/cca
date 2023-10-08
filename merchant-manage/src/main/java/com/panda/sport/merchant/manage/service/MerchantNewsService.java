package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.vo.MerchantAmountAlertVO;
import com.panda.sport.merchant.common.vo.UserLimitNewsVO;

/**
 * @author YK
 * 商户系统消息服务
 * @date 2020/3/12 17:00
 */
public interface MerchantNewsService extends IService<MerchantNews> {
    /**
     * 添加一条我的消息-用户限额了
     */
    @Deprecated
    void addUserLimitNews(UserLimitNewsVO userLimitNews);

    /**
     * 添加一条商户额度使用预警消息
     */
    void addMerchantAlertFromUsedAmount(MerchantAmountAlertVO merchantAmountAlert);
}
