package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.dto.InfomationOf2TimesOrderDTO;
import com.panda.sport.merchant.common.po.bss.OrderTimesSettleInfoPO;
import com.panda.sport.merchant.common.vo.OrderOverLimitVO;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
import com.panda.sport.merchant.common.vo.api.MatchVO;
import com.panda.sport.merchant.common.vo.api.ReportApiVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupOrderMapper {

    Integer countlBetListByUpdateTime(@Param(value = "merchantCode") String merchantCode, @Param(value = "startTimeL") long startTimeL,
                                      @Param(value = "endTimeL") long endTimeL);

    List<BetApiVo> queryAllBetListByUpdateTime(@Param(value = "merchantCode") String merchantCode,
                                               @Param(value = "startTimeL") long startTimeL, @Param(value = "endTimeL") long endTimeL,
                                               @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize);

    Integer countSettleOrderListBackUp(@Param(value = "merchantCode") String merchantCode, @Param(value = "startTimeL") long startTimeL,
                                       @Param(value = "endTimeL") long endTimeL, @Param(value = "vipLevel") Integer vipLevel);

    /**
     *
     */
    Integer countTicketListBackUp(@Param(value = "merchantCode") String merchantCode, @Param(value = "startTimeL") long startTimeL,
                                       @Param(value = "endTimeL") long endTimeL, @Param(value = "vipLevel") Integer vipLevel);

    List<BetApiVo> querySettleOrderListBackUp(@Param(value = "merchantCode") String merchantCode,
                                              @Param(value = "startTimeL") long startTimeL, @Param(value = "endTimeL") long endTimeL,
                                              @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,
                                              @Param(value = "vipLevel") Integer vipLevel);

    /**
     * vip注单
     */
    List<BetApiVo> queryTicketListBackUp(@Param(value = "merchantCode") String merchantCode,
                                              @Param(value = "startTimeL") long startTimeL, @Param(value = "endTimeL") long endTimeL,
                                              @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,
                                              @Param(value = "vipLevel") Integer vipLevel);

    ReportApiVo getMerchantStatistic(@Param(value = "startTimeL") Long startTime, @Param(value = "endTimeL") Long endTime, @Param(value = "merchantCode") String merchantCode);


    BetApiVo getBetDetailBackUp(@Param(value = "merchantCode") String merchantCode, @Param(value = "orderNo") String orderId);

    List<BetApiVo> queryBetListBackUp(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                                      @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId,
                                      @Param(value = "tournamentId") Long tournamentId, @Param(value = "orderStatus") Integer orderStatus,
                                      @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize, @Param(value = "orderBy") Integer orderBy);

    Integer countBetListBackUp(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                               @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId,
                               @Param(value = "tournamentId") Long tournamentId, @Param(value = "orderStatus") Integer orderStatus, @Param(value = "orderBy") Integer orderBy);


    List<BetApiVo> queryBetListBackUpByLg(@Param(value = "userName") String userName, @Param(value = "startTime") Long startTime, @Param(value = "endTime") Long endTime,
                                          @Param(value = "merchantCode") String merchantCode, @Param(value = "sportId") Integer sportId, @Param(value = "tournamentId") Long tournamentId,
                                          @Param(value = "orderStatus") Integer orderStatus, @Param(value = "start") Integer start, @Param(value = "size") Integer pageSize,
                                          @Param(value = "orderBy") Integer orderBy, @Param(value = "language") String language);
    OrderOverLimitVO queryOneOrderOverLimitInfo(@Param("orderNo") String orderNo);

    List<OrderOverLimitVO> queryOrderOverLimitInfos(List<String> orderNos);

    List<InfomationOf2TimesOrderDTO>  select2TimesOrderInfo(@Param("start") Integer start,@Param("end") Integer end);

    void batchInfomationOf2TimesOrderDTO(List<OrderTimesSettleInfoPO> tOrderTimesSettleInfoList);

    MatchVO selectMatchInfo(@Param("matchId") Long matchId);
}
