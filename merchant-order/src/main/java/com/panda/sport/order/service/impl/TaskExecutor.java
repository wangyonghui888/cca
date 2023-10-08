package com.panda.sport.order.service.impl;


import com.panda.sport.backup.mapper.BackupOrderMapper;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TaskExecutor implements Runnable {
    private CountDownLatch latch;
    private int threadNum;

    private Map<Integer, Object> resultMap;

    private BackupOrderMapper orderMapper;
    private String merchantCode;
    private Long startTime;
    private Long endTime;
    private Integer startIndex;
    private Integer endIndex;
    private Integer pageSize;

    private Integer vipLevel;

    private Boolean largerThanSevenDay;


    public TaskExecutor(CountDownLatch latch, int threadNum, Map<Integer, Object> resultMap, BackupOrderMapper orderMapper, String merchantCode,
                        Long startTime, Long endTime, Integer startIndex, Integer endIndex, Integer pageSize, Integer vipLevel, Boolean largerThanSevenDay) {
        this.latch = latch;
        this.threadNum = threadNum;
        this.resultMap = resultMap;
        this.orderMapper = orderMapper;
        this.merchantCode = merchantCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.pageSize = pageSize;

        this.vipLevel = vipLevel;
        this.largerThanSevenDay = largerThanSevenDay;
    }

    @Override
    public void run() {
        try {
            log.info("UPLOAD_线程,merchantCode:{},线程:{},准备干活.........", threadNum, merchantCode);
            List<BetApiVo> resultList = new ArrayList<>();
            int total = endIndex - startIndex;
            int pageNo = 1;
            int n = (total % pageSize) == 0 ? (total / pageSize) : (total / pageSize + 1);
            for (int i = 0; i < n; i++) {
                int start = (pageNo - 1) * pageSize + startIndex;
                int size = (i + 1 == n) ? (endIndex - start + 1) : pageSize;
                log.info("UPLOAD_线程,merchantCode=" + merchantCode + ",线程=" + threadNum + ",start=" + start + ",size=" + size + ",n=" + i);
                if (largerThanSevenDay) {
                    List<BetApiVo> betApiVoList;
                    try {
                        betApiVoList = orderMapper.queryTicketListBackUp(merchantCode, startTime, endTime, start, size, vipLevel);
                    } catch (Exception e) {
                        log.error("SQL超时，再执行一次，线程:" + threadNum + ",n=" + i, e);
                        betApiVoList = orderMapper.queryTicketListBackUp(merchantCode, startTime, endTime, start, size, vipLevel);
                    }
                    resultList.addAll(betApiVoList);
                } else {
                    List<BetApiVo> betApiVoList = orderMapper.querySettleOrderListBackUp(merchantCode, startTime, endTime, start, size, vipLevel);
                    resultList.addAll(betApiVoList);
                }
                pageNo += 1;
            }
            resultMap.put(threadNum, resultList);
            log.info("UPLOAD_线程,merchantCode=" + merchantCode + ",线程=" + threadNum + ",干活完毕1!!共:" + resultList.size());
        } catch (Exception e) {
            log.error("UPLOAD_线程栅栏报错1!" + merchantCode, e);
        } finally {
            //当前计算工作已结束，计数器减一
            latch.countDown();
        }
    }
}
