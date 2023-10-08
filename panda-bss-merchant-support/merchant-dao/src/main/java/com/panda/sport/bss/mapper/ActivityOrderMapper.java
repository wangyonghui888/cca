package com.panda.sport.bss.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository(value = "bssActivityOrderMapper")
public interface ActivityOrderMapper {

    List<Long> queryDailyBetTimesUsers1(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "betAmount") Long betAmount,
                                        @Param(value = "sportList") List<Integer> sportList, @Param(value = "playList") List<Integer> playList);

    List<Map<String, Object>> queryDailyBetTimesUsers2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Long betAmount,
                                                       @Param(value = "sportList") List<Integer> sportList, @Param(value = "playList") List<Integer> playList, @Param(value = "times") Integer times,
                                                       @Param(value = "userList") List<Long> userList);

    List<Map<String, Object>> queryBetAmountUser(@Param(value = "betAmount") BigDecimal betAmount, @Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL,
                                                 @Param(value = "sportList") List<Integer> sportList, @Param(value = "playList") List<Integer> playList);

    List<Long> queryDailyVirtualBetTimesUsers1(@Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL, @Param(value = "betAmount") Long betAmount,
                                               @Param(value = "sportList") List<Integer> sportList);

    List<Map<String, Object>> queryDailyVirtualBetTimesUsers2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL,
                                                              @Param(value = "betAmount") Long betAmount, @Param(value = "sportList") List<Integer> sportList,
                                                              @Param(value = "times") Integer times, @Param(value = "userList") List<Long> userList);

    List<Long> querySeriesTimesUser1(@Param(value = "betAmount") Double betAmount, @Param(value = "times") Integer times,
                                     @Param(value = "startTimeL") Long startL, @Param(value = "endTimeL") Long endL);

    List<Map<String, Object>> querySeriesTimesUser2(@Param(value = "startTimeL") Long datStartL, @Param(value = "endTimeL") Long datEndL, @Param(value = "betAmount") Double betAmount, @Param(value = "times") Integer times,
                                                    @Param(value = "userList") List<Long> userList);

    int queryLatestOrderCount(@Param(value = "startTimeL") long startL, @Param(value = "endTimeL") long endL);
}
