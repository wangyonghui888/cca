package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.OrderOverLimitVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.user.OrderVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserOrderService {

    Response<Object> userOrderAll(String userId);

    Response<Object> userProfitAll(String userId);

    Response<Object> userOrderMonth(String userId);

    Response<Object> userOrderOneMonthDays(String userId, Integer time);

    Response<?> queryUserBetList(UserOrderVO vo, String language,Integer isExport);

    Response<Object> queryHotPlayName(Integer sportId, String language);

    Response<?> queryUserBetListByTime(UserOrderVO vo);

    Response<?> listUserBetGroupByUser(UserOrderVO vo);

    void export(String username, HttpServletRequest request, UserOrderVO vo);


    void queryUserBetListExport(String username, HttpServletRequest request, UserOrderVO vo);


    Response<Object> queryUserOrderList(UserOrderVO userId);

    void exportUserOrder(HttpServletResponse response, HttpServletRequest request, UserOrderVO vo) throws Exception;

    Response<?> importVipUser(HttpServletResponse response, HttpServletRequest request, MultipartFile vipCsv);

    Response<?> uploadVipUser(HttpServletResponse response, HttpServletRequest request, String vipuserls, Integer type);

    Response<?> importToDisabled(HttpServletResponse response, HttpServletRequest request, String userIds, Integer disabled,String remark);

    Response<?> updateDisabled(HttpServletResponse response, HttpServletRequest request, String userId, Integer disabled);

    Response<?> findUserInfo(UserVipVO vo);

    Response<Object> getStatistics(UserOrderVO vo) throws Exception;

    Response<Object> getStatisticsES(UserOrderVO vo) throws Exception;
    Response<?> queryBetToday();


    Response<?> userOrderDay30();

    Response<?> userDaySpread14(UserOrderVO vo);

    Response<?> queryUserToday();


    Response<?> queryUserList(UserOrderVO vo);

    Response<?> getUserDetail(String uid);

    Response<Object> queryTicketList(UserOrderVO vo, String language);


    Response<Object> queryTicketListES(UserOrderVO vo, String language);

    Response<Object> queryAppointmentList(UserOrderVO vo, String language);

    Response<Object> queryPreSettleOrder(UserOrderVO userOrderVO);

    Map exportTicketList(String username, UserOrderVO vo, String language);

    Map exportTicketListES(String username, UserOrderVO vo, String language);

    Map exportTicketAccountHistoryList(String username, UserOrderVO vo, String language);

    Response getMerchantOrderDay();

    Response merchantUser();

    Response queryFakeNameByCondition(String fakeName);

    Response queryOneOrderOverLimitInfo(String orderNo);

    Response<List<OrderOverLimitVO>> queryOrderOverLimitInfos(List<String> orderNos);

    Response<?> updateIsVipDomain(String userId, Integer isVipDomain);

    List<Long> getUserIdListByParam(Map<String, Object> paramMap);

    int getUserCountByParam(Map<String, Object> paramMap);

    List<OrderVO> getOrderList(Map<String, Object> paramMap);

    void execOrderBySToRedisTask(String param);

}
