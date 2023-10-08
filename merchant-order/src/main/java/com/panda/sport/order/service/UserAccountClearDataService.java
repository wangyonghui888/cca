package com.panda.sport.order.service;

import org.springframework.stereotype.Repository;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.order.service
 * @Description :  活动
 * @Date: 2021-08-21 11:12
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Repository
public interface UserAccountClearDataService {

    void userAccountClearDataTask(Integer num);

    void userAccountAllClearDataTask(Integer num);

    void acUserOrderDateClearDataTask(Long nowL);

    void userOrderDayClearDataTask(Integer num);

    void userOrderDayUtfClearDataTask(Integer num);

    void userOrderMonthClearDataTask(Integer num);

    void userOrderMonthUtfClearDataTask(Integer num);

    void userSummaryLibraryDataTask(Integer valueOf);
}
