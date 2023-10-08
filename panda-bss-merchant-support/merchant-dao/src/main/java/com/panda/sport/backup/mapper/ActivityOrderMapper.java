package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.vo.AcTaskParamVO;
import com.panda.sport.merchant.common.vo.MatchUserMidVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ActivityOrderMapper {


    List<Long> queryDailyBetTimesUsers1(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "betAmount") Long betAmount, @Param(value = "sportList") List<Integer> sportList, @Param(value = "playList") List<Integer> playList);

    List<Map<String, Object>> queryDailyBetTimesUsers2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Long betAmount, @Param(value = "sportList") List<Integer> sportList, @Param(value = "playList") List<Integer> playList, @Param(value = "times") Integer times, @Param(value = "userList") List<Long> userList);

    List<Map<String, Object>> queryBetAmountUser(@Param(value = "betAmount") BigDecimal betAmount, @Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "sportList") List<Integer> sportList, @Param(value = "playList") List<Integer> playList);

    List<Long> queryDailyVirtualBetTimesUsers1(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "betAmount") Long betAmount, @Param(value = "sportList") List<Integer> sportList);

    List<Map<String, Object>> queryDailyVirtualBetTimesUsers2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Long betAmount, @Param(value = "sportList") List<Integer> sportList, @Param(value = "times") Integer times, @Param(value = "userList") List<Long> userList);

    List<Long> querySeriesTimesUser1(@Param(value = "betAmount") Double betAmount, @Param(value = "times") Integer times, @Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL);

    List<Map<String, Object>> querySeriesTimesUser2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Double betAmount, @Param(value = "times") Integer times, @Param(value = "userList") List<Long> userList);

    int queryLatestOrderCount(@Param(value = "startTimeL") long startL, @Param(value = "endTimeL") long endL);

    Set<Long> getDailyAcTaskBonusUser(@Param(value = "startL") Long startL, @Param(value = "endL") Long endL, @Param(value = "userList") List<Long> userList, @Param(value = "item") AcTaskParamVO paramVOs);

    Set<Long> getDailyAcTaskBonusUser210(@Param(value = "startL") Long startL, @Param(value = "endL") Long endL, @Param(value = "userList") List<Long> userList, @Param(value = "item") AcTaskParamVO paramVOs);

    List<Map<String, Object>> getDayDailyAcTaskBonusUser(@Param(value = "datStartL") Long datStartL, @Param(value = "datEndL") Long datEndL, @Param(value = "userList") Set<Long> userList, @Param(value = "item") AcTaskParamVO paramVOs);

    List<Map<String, Object>> query4SeriesTimesUser(@Param(value = "datStartL") Long datStartL, @Param(value = "datEndL") Long datEndL, @Param(value = "userList") Set<Long> userList, @Param(value = "acTaskParamVO") AcTaskParamVO acTaskParamVO);

    Set<Long> getDaily4AcTaskBonusUserList(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "userList") List<Long> userList, AcTaskParamVO paramVOs);

    List<MatchUserMidVO> queryMatchUserMidInfoList(@Param(value = "startL") long startL, @Param(value = "endL") long endL);

    void saveMatchUserMidInfoList(@Param(value = "list") List<MatchUserMidVO> mList);

    List<Long> getDaily6AcTaskBonusUser(@Param(value = "startL") Long startL, @Param(value = "endL") Long endL, @Param(value = "userList") List<Long> userList, @Param(value = "haveUserList") List<Long> haveUserList);

    List<Map<String, Object>> queryRegisterTimesUserSix(@Param(value = "userList") List<Long> userList, @Param(value = "item") AcTaskParamVO acTaskParamVO, @Param(value = "haveUserList") List<Long> haveUserList);

    List<Long> getHaveTaskBonusUser(@Param(value = "taskId") Integer taskId, @Param(value = "timeL") Long timeL);
}