package com.panda.center.mapper.activity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface UserReport3Mapper {

    List<Map<String, Object>> queryOlympicTimesUser(@Param("times") Integer times, @Param("timeL") Long timeL);

    List<Map<String, Object>> queryOlympicSeriesTimesUser(@Param("times") Integer times, @Param("timeL") Long timeL, @Param("betAmount") Double betAmount);

    List<Map<String, Object>> queryOlympicBetAmountUser(@Param("betAmount") BigDecimal betAmount, @Param("timeL") Long timeL);

    List<Map<String, Object>> queryOlympicPlayTimesUser(@Param("playId") String playId, @Param("times") Integer times, @Param("timeL") Long timeL);


    List<Map<String, Object>> queryUserRank(@Param("merchantCode") String merchantCode);

    List<Map<String, Object>> queryOlympicMardiGras(@Param("betAmount") BigDecimal betAmount, @Param("startTimeL") Long startDateL,
                                                    @Param("endTimeL") Long endDateL, @Param("startHourTimeL") Long startHourTimeL);

    Map<String, Object> queryOlympicEvenyDayByUser(@Param("startTimeL") Long startDateL,
                                                   @Param("endTimeL") Long endDateL, @Param("userId") Long userId);

    List<Map<String, Object>> queryOlympicBetEvery(@Param("betAmount") BigDecimal betAmount, @Param("startTimeL") Long startDateL, @Param("endTimeL") Long endDateL,
                                                   @Param("startHourTimeL") Long startHourTimeL, @Param("days") Integer days);
}
