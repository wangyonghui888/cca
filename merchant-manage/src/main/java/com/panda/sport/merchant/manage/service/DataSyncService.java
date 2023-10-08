package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据同步
 */
public interface DataSyncService {

    /**
     * 对账数据同步
     * @param startDate
     * @param endDate
     * @return
     */
    Response<?> callExecuteFinance(HttpServletRequest request,String startDate, String endDate);

    /**
     * 用户投注统计数据同步
     * @param startDate
     * @param endDate
     * @return
     */
    Response<?> callExecuteUserBet(HttpServletRequest request,String startDate, String endDate);

    /**
     * 赛事投注统计数据同步
     * @param startDate
     * @param endDate
     * @return
     */
    Response<?> callExecuteMatchBet(HttpServletRequest request,String startDate, String endDate);
}
