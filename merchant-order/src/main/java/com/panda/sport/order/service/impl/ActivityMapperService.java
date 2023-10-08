package com.panda.sport.order.service.impl;


import com.panda.sport.backup.mapper.ActivityOrderMapper;
import com.panda.sport.backup.mapper.BackUpAcBonusMapper;
import com.panda.sport.bss.mapper.AcBonusMapper;

import com.panda.sport.order.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("activityMapperService")
public class ActivityMapperService {

    @Autowired
    private AcBonusMapper acBonusMapper;

    @Autowired
    private BackUpAcBonusMapper backUpAcBonusMapper;

    @Autowired
    private ActivityOrderMapper activityOrderMapper;


    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Long> queryDailyBetTimesUsers1(Long startL, Long endL, Long betAmount,
                                               List<Integer> sportList, List<Integer> playList) {
        List<Long> userList = activityOrderMapper.queryDailyBetTimesUsers1(startL, endL, betAmount, sportList, playList);
        return userList;
    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Map<String, Object>> queryDailyBetTimesUsers2(Long datStartL, Long datEndL, Long betAmount, List<Integer> sportList, List<Integer> playList, Integer times,
                                                              List<Long> userList) {
        List<Map<String, Object>> resultList = activityOrderMapper.queryDailyBetTimesUsers2(datStartL, datEndL, betAmount, sportList, playList, times, userList);
        return resultList;

    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Long> queryAllReceivedUserListByTime(Integer taskId, Long timeL, List<Long> userIdList) {
        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
        return receivedList;
    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Long> queryDailyVirtualBetTimesUsers1(Long startL, Long endL, Long betAmount,
                                                      List<Integer> sportList) {
        List<Long> userList = activityOrderMapper.queryDailyVirtualBetTimesUsers1(startL, endL, betAmount, sportList);
        return userList;
    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Map<String, Object>> queryDailyVirtualBetTimesUsers2(Long datStartL, Long datEndL,
                                                                     Long betAmount, List<Integer> sportList,
                                                                     Integer times, List<Long> userList) {
        List<Map<String, Object>> resultList = activityOrderMapper.queryDailyVirtualBetTimesUsers2(datStartL, datEndL, betAmount, sportList, times, userList);
        return resultList;

    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Map<String, Object>> queryBetAmountUser(BigDecimal betAmount, Long startL, Long endL,
                                                        List<Integer> sportList, List<Integer> playList) {
        List<Map<String, Object>> userList = activityOrderMapper.queryBetAmountUser(betAmount, startL, endL, sportList, playList);

        return userList;
    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    public List<Long> querySeriesTimesUser1(Double betAmount, Integer times, Long startL, Long endL) {
        List<Long> userList = activityOrderMapper.querySeriesTimesUser1(betAmount, times, startL, endL);
        return userList;
    }

    @Transactional(transactionManager = DataSourceConfig.BACKUP_TRANSACTION_MANAGER)
    List<Map<String, Object>> querySeriesTimesUser2(Long datStartL, Long datEndL, Double betAmount, Integer times,
                                                    List<Long> userList) {
        List<Map<String, Object>> resultList = activityOrderMapper.querySeriesTimesUser2(datStartL, datEndL, betAmount, times, userList);
        return resultList;
    }

}
