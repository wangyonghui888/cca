package com.panda.sport.backup.mapper;


import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Repository
public interface OrderMixExportMapper {
    
    Integer countBetOrderList(BetOrderVO betOrderVO);

    
    <T> List<T> queryBetOrderList(BetOrderVO betOrderVO);

    
    Map<String, Object> getBeginTimeStatistics(BetOrderVO betOrderVO);

    
    Integer countSettledOrderList(BetOrderVO betOrderVO);

    
    <T> List<T> querySettledOrderList(BetOrderVO betOrderVO);

    
    Integer countLiveOrderList(BetOrderVO betOrderVO);

    
    List<OrderSettle> queryLiveOrderList(BetOrderVO betOrderVO);

    
    List<Map<String, String>> queryOrderExportList(@Param("createTime") Long createTime,
                                                   @Param("endTime") Long endTime,
                                                   @Param("merchantId") Long merchantId,
                                                   @Param("managerCode") Integer managerCode, @Param("language") String language,@Param("vipLevel") Integer vipLevel,@Param("currency") String currency );

    
    List<Map<String, String>> queryOrderExportSettleList(@Param("createTime") Long createTime,
                                                         @Param("endTime") Long endTime,
                                                         @Param("merchantId") Long merchantId,
                                                         @Param("managerCode") Integer managerCode, @Param("language") String language,@Param("vipLevel") Integer vipLevel,@Param("currency") String currency );

    List<Map<String, String>> queryAmountByOrderSettleTimesInfo(@Param("orderNoList") List<String> orderNoList);
}