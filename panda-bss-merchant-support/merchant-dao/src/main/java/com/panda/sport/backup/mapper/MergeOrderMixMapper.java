package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.po.bss.OrderStatisticsPO;
import com.panda.sport.merchant.common.vo.BetOrderVO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public interface MergeOrderMixMapper {

    <T> List<T> queryBetOrderList(BetOrderVO betOrderVO);

    Integer countBetOrderList(BetOrderVO betOrderVO);

    Map<String, Object> getStatistics(BetOrderVO betOrderVO);

    List<Map<String, String>> queryOrderExportSettleList(@Param("createTime") Long createTime,
                                                         @Param("endTime") Long endTime,
                                                         @Param("merchantId") Long merchantId,
                                                         @Param("managerCode") Integer managerCode,
                                                         @Param("language") String language,
                                                         @Param("vipLevel") Integer vipLevel,
                                                         @Param("currency") String currency,
                                                         @Param("start") Integer start,
                                                         @Param("size") Integer size);

    List<Map<String, String>> queryOrderExportList(@Param("createTime") Long createTime,
                                                   @Param("endTime") Long endTime,
                                                   @Param("merchantId") Long merchantId,
                                                   @Param("managerCode") Integer managerCode,
                                                   @Param("language") String language,
                                                   @Param("vipLevel") Integer vipLevel,
                                                   @Param("currency") String currency,
                                                   @Param("start") Integer start,
                                                   @Param("size") Integer size );

    Integer countOrderExportSettleList(@Param("createTime") Long createTime,
                                                         @Param("endTime") Long endTime,
                                                         @Param("merchantId") Long merchantId,
                                                         @Param("managerCode") Integer managerCode, @Param("language") String language,@Param("vipLevel") Integer vipLevel,@Param("currency") String currency );

    Integer countOrderExportList(@Param("createTime") Long createTime,
                                                   @Param("endTime") Long endTime,
                                                   @Param("merchantId") Long merchantId,
                                                   @Param("managerCode") Integer managerCode, @Param("language") String language,@Param("vipLevel") Integer vipLevel,@Param("currency") String currency );


    <T> List<T> queryUserMerchantPOList(@Param("uidList") Set<Long> uidList);

    List<Map<String,String>> querySorceByMatchIds(@Param("matchIdList") Set<String> matchIdList);

    List<OrderStatisticsPO> getOrderStatistics(BetOrderVO betOrderVO);
}