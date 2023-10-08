package com.panda.sport.admin.service;


import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PlayerOrderService {
    Response getPlayerAllOrder(String userId);

    Response userOrderMonth(String userId);

    Response userOrderMonthDays(String userId, Integer time);

    Response getPlayerProfit(String userId);

    Response<?> listUserBetGroupByUser(UserOrderVO vo);

    Response<?> queryPlayerBetReportList(UserOrderVO vo, String language);

    Response<Object> getOrderStatistics(UserOrderVO vo);

    Response<Object> getStatisticsES(UserOrderVO vo);
    Response<Object> queryUserOrderList(UserOrderVO vo);

    Response<Object> queryTicketList(UserOrderVO vo, String language);

    void export(HttpServletRequest request, HttpServletResponse response);

    void exportUserOrder(UserOrderVO user, HttpServletResponse response);

    Response<Object> getUserInfo(String uid);

    Response<?> queryPlayerTop10();

    Response<Object> queryHotPlayName(Integer sportId, String language);

    Response<Object> exportTicketList(UserOrderVO vo, String token, String language);

    Response<Object> exportTicketAccountHistoryList(UserOrderVO vo, String token, String language);

    Response queryBetToday();

    Response queryUserToday();

    Response merchantMatchTop10();

    Response merchantOrder();

    Response userDaySpread30();

    Response<?> queryUserBetListByTime(UserOrderVO vo);

    Response getSportList(String language);

    Response<Object> queryPreSettleOrder(UserOrderVO userOrderVO);

    /**
     * 根据用户名查询用户ID列表
     * @param vo
     * @return Response
     */
    Response queryUserIdListByUserName(UserOrderVO vo);

    /**
     * 查询预约模式
     * @param vo
     * @return Response
     */
    Response queryAppointmentList(UserOrderVO vo, String language);

    Response<Object> queryTicketListES(UserOrderVO vo, String language);

    Response<Object> exportTicketListES(UserOrderVO vo, String token, String language);

}
