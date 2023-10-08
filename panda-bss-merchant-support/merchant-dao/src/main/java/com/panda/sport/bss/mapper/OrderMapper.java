package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.vo.OrderOverLimitVO;
import com.panda.sport.merchant.common.vo.StatisticsVO;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
import com.panda.sport.merchant.common.vo.api.ReportApiVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderMapper {

    BetApiVo getBetDetail(@Param(value = "merchantCode") String merchantCode, @Param(value = "orderNo") String orderId);

    List<BetApiVo> queryBetList(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                                @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId,
                                @Param(value = "tournamentId") Long tournamentId, @Param(value = "orderStatus") Integer orderStatus,
                                @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize, @Param(value = "orderBy") Integer orderBy);

    List<BetApiVo> queryBetListV2(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                                @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId,
                                @Param(value = "tournamentId") Long tournamentId, @Param(value = "orderStatus") Integer orderStatus,
                                @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,@Param(value = "orderBy") Integer orderBy);


    Integer countBetList(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                         @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId,
                         @Param(value = "tournamentId") Long tournamentId, @Param(value = "orderStatus") Integer orderStatus, @Param(value = "orderBy") Integer orderBy);


    Integer countSettleOrderList(@Param(value = "merchantCode") String merchantCode, @Param(value = "startTimeL") long startTimeL,
                                 @Param(value = "endTimeL") long endTimeL);

    List<BetApiVo> querySettleOrderList(@Param(value = "merchantCode") String merchantCode,
                                        @Param(value = "startTimeL") long startTimeL, @Param(value = "endTimeL") long endTimeL,
                                        @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize);

    List<BetApiVo> queryBetListBackUpByLg(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                                          @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId, @Param(value = "tournamentId") Long tournamentId,
                                          @Param(value = "orderStatus") Integer orderStatus, @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,
                                          @Param(value = "orderBy") Integer orderBy, @Param(value = "language") String language);


    List<BetApiVo> queryBetListBackUpByLgV2(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                                          @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId, @Param(value = "tournamentId") Long tournamentId,
                                          @Param(value = "orderStatus") Integer orderStatus, @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,
                                          @Param(value = "orderBy") Integer orderBy, @Param(value = "language") String language);


    Integer countSettleOrderListBackUp(@Param(value = "merchantCode") String merchantCode, @Param(value = "startTimeL") long startTimeL,
                                       @Param(value = "endTimeL") long endTimeL, @Param(value = "vipLevel") Integer vipLevel);

    List<BetApiVo> querySettleOrderListBackUp(@Param(value = "merchantCode") String merchantCode,
                                              @Param(value = "startTimeL") long startTimeL, @Param(value = "endTimeL") long endTimeL,
                                              @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,
                                              @Param(value = "vipLevel") Integer vipLevel);

    Integer countlBetListByUpdateTime(@Param(value = "merchantCode") String merchantCode, @Param(value = "startTimeL") long startTimeL,
                                      @Param(value = "endTimeL") long endTimeL);

    List<BetApiVo> queryAllBetListByUpdateTime(@Param(value = "merchantCode") String merchantCode,
                                               @Param(value = "startTimeL") long startTimeL, @Param(value = "endTimeL") long endTimeL,
                                               @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize);

    ReportApiVo getMerchantStatistic(@Param(value = "startTimeL") Long startTime, @Param(value = "endTimeL") Long endTime, @Param(value = "merchantCode") String merchantCode);


    StatisticsVO countPreBetOrderSize(@Param("param") Map<String, Object> paramMap);

    List<BetApiVo> getPreBetOrderList(@Param("param") Map<String, Object> paramMap);
}