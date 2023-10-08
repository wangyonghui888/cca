package com.panda.sport.backup83.mapper;

import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.OrderPreSettleDTO2;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import com.panda.sport.merchant.common.vo.user.OrderVO;
import com.panda.sport.merchant.common.vo.user.UserFakeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public interface Backup83OrderMixMapper {

    <T> List<T> queryBetOrderList(BetOrderVO betOrderVO);

    <T> List<T> queryPreBetOrderList(BetOrderVO betOrderVO);

    List<Map<String, Object>> queryBetOrderAccountHistoryList(BetOrderVO betOrderVO);


    Integer countBetOrderList(BetOrderVO betOrderVO);

    Integer countPreBetOrderList(BetOrderVO betOrderVO);

    Map<String, Object> getStatistics(BetOrderVO betOrderVO);


    Map<String, Object> getSettleStatistics(BetOrderVO betOrderVO);


    Map<String, Object> getBeginTimeStatistics(BetOrderVO betOrderVO);

    List<Map<String, Object>> queryPlayOptionList(@Param(value = "playOptionList") List<Long> playOptionList, @Param(value = "language") String language);


    <T> List<T> querySettledOrderList(BetOrderVO betOrderVO);

    <T> List<T> querySettledOrderListByModifyTime(BetOrderVO betOrderVO);


    List<Map<String, Object>> querySettleOrderAccountHistoryList(BetOrderVO betOrderVO);


    List<OrderSettle> queryLiveOrderList(BetOrderVO betOrderVO);


    List<Map<String, Object>> queryLiveOrderAccountHistoryList(BetOrderVO betOrderVO);

    Integer countSettledOrderList(BetOrderVO betOrderVO);

    Integer countSettledOrderListByModifyTime(BetOrderVO betOrderVO);

    Integer countLiveOrderList(BetOrderVO betOrderVO);


    List<Map<String, Object>> getSettleStatisticsWithRate(BetOrderVO betOrderVO);


    List<CurrencyRatePO> queryCurrencyRateList();

    CurrencyRatePO queryCurrencyRateByCode(@Param("currencyCode") String currencyCode);


    List<Map<String, Object>> queryBcOrderRate(@Param("startTimeL") Long startTimeL, @Param("endTimeL") Long endTimeL);


    String getMatchInfo(@Param("matchId") Long matchId, @Param("language") String language);


    List<UserFakeVO> queryFakeNameByCondition(@Param("fakeName") String fakeName);


    @Select("select ${language} from s_virtual_language where name_code = #{nameCode}")
    String getNameByNameCode(@Param("language") String language, @Param("nameCode") String nameCode);


    List<OrderPO> getOrderByOrderNos(UserOrderVO userOrderVO);

    List<OrderDetailPO> getOrderDetailByOrderNos(UserOrderVO userOrderVO);

    List<TPreOrderDetailPO> getTPreOrderDetailPO(UserOrderVO userOrderVO);

    List<OrderPreSettleDTO2> getOrderPreSettleList(UserOrderVO userOrderVO);

    <T> List<T> queryUserMerchantPOList(@Param("uidList") Set<Long> uidList);

    List<OrderVO> getOrderList(@Param("param") Map<String, Object> param);

    List<OrderStatisticsPO> getOrderSettleStatistics(BetOrderVO betOrderVO);

    List<OrderStatisticsPO> getBeginTimeOrderStatistics(BetOrderVO betOrderVO);

    List<OrderStatisticsPO> getOrderStatistics(BetOrderVO betOrderVO);
}