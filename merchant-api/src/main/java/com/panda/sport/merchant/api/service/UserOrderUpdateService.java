package com.panda.sport.merchant.api.service;

import com.panda.sport.merchant.common.dto.UserOrderTimePO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;

import java.util.List;


public interface UserOrderUpdateService {

    int updateUserOrderTime(List<UserOrderTimePO> list);

    int updateUserSevenOrder(List<UserOrderTimePO> list);

    int updateUserAllLifeData(List<UserOrderTimePO> list);
    int updateUserAllStatistic(List<UserOrderDayPO> list);

    int upsertUserVip(String merchantCode, List<UserVipVO> list);

    int upsertUserDisabled(String merchantCode, List<UserVipVO> list);
    int upsertUserDisabled2Allow(String merchantCode,Integer disabled, List<UserVipVO> list);

    int updateDisabled(String merchantCode, UserVipVO userVipVO);

    void updateIsVipDomain(String merchantCode, String userId, Integer isVipDomain);

    void upsertUserToDisabled(String merchantCode, List<Long> uidList, Integer disabled);
}
