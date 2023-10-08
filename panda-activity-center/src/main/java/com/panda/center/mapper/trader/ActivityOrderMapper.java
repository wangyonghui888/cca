package com.panda.center.mapper.trader;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface ActivityOrderMapper {


    List<Long> queryDJDailyBetTimesUsers1(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "betAmount") Long betAmount,
                                          @Param(value = "sportList") List<Long> sportList, @Param(value = "playList") List<Integer> playList);

    List<Map<String, Object>> queryDJDailyBetTimesUsers2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Long betAmount,
                                                         @Param(value = "sportList") List<Long> sportList, @Param(value = "playList") List<Integer> playList, @Param(value = "times") Integer times,
                                                         @Param(value = "userList") List<Long> userList);

    List<Map<String, Object>> queryDJBetAmountUser(@Param(value = "betAmount") BigDecimal betAmount, @Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL,
                                                   @Param(value = "sportList") List<Long> sportList, @Param(value = "playList") List<Integer> playList);

    List<Long> queryDJDailyVirtualBetTimesUsers1(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "betAmount") Long betAmount,
                                                 @Param(value = "sportList") List<Long> sportList);

    List<Map<String, Object>> queryDJDailyVirtualBetTimesUsers2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL,
                                                                @Param(value = "betAmount") Long betAmount, @Param(value = "sportList") List<Long> sportList,
                                                                @Param(value = "times") Integer times, @Param(value = "userList") List<Long> userList);

    List<Long> queryDJSeriesTimesUser1(@Param(value = "betAmount") Double betAmount, @Param(value = "times") Integer times,
                                       @Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "orderType") Integer orderType);

    List<Map<String, Object>> queryDJSeriesTimesUser2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Double betAmount, @Param(value = "times") Integer times,
                                                      @Param(value = "userList") List<Long> userList, @Param(value = "orderType") Integer orderType);
}