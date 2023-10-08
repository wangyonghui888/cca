package com.panda.sport.merchant.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.ActivityOrderMapper;
import com.panda.sport.bss.mapper.BackUpAcBonusMapper;
import com.panda.sport.bss.mapper.AcBonusMapper;
import com.panda.sport.bss.mapper.ActivityMerchantMapper;
import com.panda.sport.merchant.api.feign.MerchantReportClient;
import com.panda.sport.merchant.api.service.ActivityService;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Slf4j
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private AcBonusMapper acBonusMapper;

    @Value("${activity.start:20210924}")
    private String acStartDay;

    @Autowired
    private ActivityMerchantMapper activityMerchantMapper;

    @Autowired
    @Qualifier(value = "bssActivityOrderMapper")
    private ActivityOrderMapper activityOrderMapper;

    @Autowired
    private MerchantReportClient reportClient;

    @Autowired
    @Qualifier(value = "bssBackUpAcBonusMapper")
    private BackUpAcBonusMapper backUpAcBonusMapper;


    @Override
    public void updateUserBonus(List<AcBonusPO> bonusList) {
        acBonusMapper.upsertUserBonus(bonusList);
    }

    @Override
    public void clearTicketsOfTask(String merchantCode) {
        log.info("ActivityServiceImpl.clearTicketsOfTask=" + merchantCode);
        acBonusMapper.clearTicketsOfTask();
    }

    @Override
    public void clearTicketsOfMardigraTask(String merchantCode, Integer conditionId) {
        log.info("ActivityServiceImpl.clearTicketsOfMardigraTask=" + merchantCode);
        acBonusMapper.clearTicketsOfMardigraTask(conditionId);
    }

    /**
     * 成长任务： 1：本月累计投注 x 元 2：本周累计有效投注 >= x 元 3：本月累计有效投注 >= x 元
     */
    @Override
    @Async
    public void executeSumTask() {
        try {
            long start = System.currentTimeMillis();
            log.info("ActivitySummerJob2开始执行成长任务:" + start);
            List<Map<String, Object>> configList = acBonusMapper.queryAllTaskConfig(1);
            log.info("ActivitySummerJob共:" + (CollectionUtils.isNotEmpty(configList) ? configList.size() : 0) + "个任务");
            if (CollectionUtils.isEmpty(configList)) return;
            int total = configList.size();
            int pageSize = 3;
            int threadNum = total > pageSize ? (int) Math.ceil((float) total / pageSize) : 1;
            if (threadNum > 16) {
                threadNum = 16;
            }
            log.info("executeSumTask,task of total=" + total + ",线程数=" + threadNum);
            CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
            try {
                List<List<Map<String, Object>>> partitionList = Lists.partition(configList, pageSize);
                for (int i = 0; i < threadNum; i++) {
                    int finalThreadNum = i;
                    List<Map<String, Object>> tempConfigList = partitionList.get(i);
                    threadPool.execute(() -> {
                        executeSumTaskThread(finalThreadNum, tempConfigList);
                        countDownLatch.countDown();
                    });
                }
                countDownLatch.await();
                log.info("executeSumTask所有活动线程都执行处理数据完毕!");
            } catch (InterruptedException e) {
                log.error("executeSumTask活动线程释放异常!", e);
            } finally {
                threadPool.shutdown();
            }
            log.info("ActivitySummerJob2任务执行成长结束:" + (System.currentTimeMillis() - start));
        }catch (Exception e){
            log.error("ActivitySummerJob2任务执行结束ERROR!", e);
        }
    }

    /**
     * 成长任务
     * @param threadNum
     * @param configList
     */
    private void executeSumTaskThread(int threadNum, List<Map<String, Object>> configList) {
        log.info("executeSumTaskThread--->" + threadNum + ",任务大小:" + configList.size());
        try {
            Date now = new Date();
            Date startMonthDate = DateUtil.beginOfMonth(now);
            Date endMonthDate = DateUtil.endOfMonth(now);
            Long startMonthDateL = Long.parseLong(DateFormatUtils.format(startMonthDate, "yyyyMMdd"));
            Long endMonthDateL = Long.parseLong(DateFormatUtils.format(endMonthDate, "yyyyMMdd"));
            Date startWeekDate = DateUtil.beginOfWeek(now);
            Date endWeekDate = DateUtil.endOfWeek(now);
            Long startWeekDateL = Long.parseLong(DateFormatUtils.format(startWeekDate, "yyyyMMdd"));
            Long endWeekDateL = Long.parseLong(DateFormatUtils.format(endWeekDate, "yyyyMMdd"));
            Long monthL = Long.parseLong(DateFormatUtils.format(now, "yyyyMM"));
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.setFirstDayOfWeek(Calendar.MONDAY);
            String weekStr = c.get(Calendar.YEAR) + "" + c.get(Calendar.WEEK_OF_YEAR);
            Long acStartDayL = getActivityStartTime();// Long.parseLong(acStartDay);
            for (Map<String, Object> config : configList) {
                String taskName = (String) config.get("taskName");
                String actName = (String) config.get("actName");
                Integer tickets = (Integer) config.get("ticketNum");
                Integer actId = (Integer) config.get("actId");
                Integer conditionId = (Integer) config.get("conditionId");
                String taskCondition = (String) config.get("taskCondition");
                String taskCondition2 = (String) config.get("taskCondition2");
                String taskCondition3 = (String) config.get("taskCondition3");
                Integer id = ((Long) config.get("id")).intValue();
                log.info("executeSumTaskThread--->" + threadNum + ",taskName=" + taskName + ",id=" + id + ",tickets=" + tickets);
                switch (conditionId) {
                    case 1:
                        betEvery(1, tickets, actName, actId, id, taskName, taskCondition, taskCondition2, startMonthDateL, endMonthDateL, monthL, acStartDayL);
                        break;
                    case 2:
                        criterierBet(2, tickets, actName, actId, id, taskName, taskCondition, taskCondition2, startWeekDateL, endWeekDateL, Long.parseLong(weekStr), acStartDayL);
                        break;
                    case 3:
                        criterierBet(3, tickets, actName, actId, id, taskName, taskCondition, taskCondition2, startMonthDateL, endMonthDateL, monthL, acStartDayL);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            log.error("executeSumTaskThread--->" + threadNum, e);
        }
    }

    private Long getActivityStartTime() {
        Long acStartDayL = Long.parseLong(acStartDay);
        try {
            Map<String, Object> summerTask = activityMerchantMapper.getSummerTaskActivity();
            if (summerTask != null) {
                Long id = (Long) summerTask.get("id");
                String name = (String) summerTask.get("name");
                Long inStartTime = (Long) summerTask.get("inStartTime");
                Long inEndTime = (Long) summerTask.get("inEndTime");
                if (inStartTime != null && inStartTime != 0) {
                    String startTimeStr = DateFormatUtils.format(new Date(inStartTime), "yyyyMMdd");
                    acStartDayL = Long.parseLong(startTimeStr);
                }
            }
        } catch (Exception e) {
            log.error("getActivityStartTime:", e);
        }
        return acStartDayL;
    }

    /**
     * 1：本月累计投注day
     * @param conditionId
     * @param tickets
     * @param actName
     * @param actId
     * @param taskId
     * @param taskName
     * @param taskCondition
     * @param taskCondition2
     * @param startMonthDateL
     * @param endMonthDateL
     * @param monthL
     * @param acStartDayL
     */
    private void betEvery(Integer conditionId, int tickets, String actName, Integer actId, Integer taskId, String taskName, String taskCondition, String taskCondition2,
                          Long startMonthDateL, Long endMonthDateL, Long monthL, Long acStartDayL) {
        try {
            Integer taskType = 1;
            if (acStartDayL > startMonthDateL) {
                startMonthDateL = acStartDayL;
            }
            log.info("taskId=" + taskId + "," + conditionId + ",betEvery.param=" + tickets + "," + actName + "," + taskCondition + "," + startMonthDateL + "," + endMonthDateL + ",updateL=" + monthL);
            int days = Integer.parseInt(taskCondition);
            double betAmount = StringUtils.isNotEmpty(taskCondition2) ? Double.parseDouble(taskCondition2) : 0;
            betAmount = 100 * betAmount;
            List<Map<String, Object>> userList = reportClient.queryOlympicBetEvery(days, betAmount, startMonthDateL, endMonthDateL);
            log.info("taskId=" + taskId + "," + conditionId + ",betEvery.queryOlympicBetEvery获取数据" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isNotEmpty(userList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : userList) {
                    String userName = (String) userMap.get("userName");
                    String merchantCode = (String) userMap.get("merchantCode");
                    Integer time = (Integer) userMap.get("days");
                    Long userId = (Long) userMap.get("uid");
                    Double valaidBetAmount = (Double) userMap.get("validBetAmount");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setMerchantCode(merchantCode);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(taskType);
                    userBonus.setTicketNum(tickets);
                    userBonus.setRemark("days=" + time + ",有效投注:" + (valaidBetAmount / 100));
                    userBonus.setLastUpdate(monthL);
                    userBonusList.add(userBonus);
                }
                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                for (Map.Entry entry : bonusMap.entrySet()) {
                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, monthL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info(merchantCode + "taskId=" + taskId + "---betEvery差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()) + reduceList);
                            if (CollectionUtils.isNotEmpty(reduceList))
                                acBonusMapper.upsertUserBonus(reduceList);
                        } else {
                            log.info(merchantCode + "taskId=" + taskId + "---betEvery插入数据---" + subBonusList.size() + subBonusList);
                            acBonusMapper.upsertUserBonus(subBonusList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("betEvery,actId=" + actId + ",task=" + taskName + "taskId=" + taskId, e);
        }
    }

    private void criterierBet(Integer conditionId, int tickets, String actName, Integer actId, Integer taskId, String taskName, String taskCondition,
                              String taskCondition2, Long startDateL, Long endDateL, Long updateL, Long acStartDayL) {
        try {
            if (acStartDayL > startDateL) {
                startDateL = acStartDayL;
            }
            log.info("taskId=" + taskId + "," + conditionId + ",criterierBet.param=" + startDateL + "," + endDateL + "," + taskCondition + "," + taskName + ",updatel=" + updateL);
            double betAmount = Double.parseDouble(taskCondition);
            betAmount = betAmount * 100;
            List<Map<String, Object>> userList = reportClient.queryOlympicMardiGras(betAmount, startDateL, endDateL);
            log.info("taskId=" + taskId + "," + conditionId + ",criterierBet=" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isNotEmpty(userList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : userList) {
                    String userName = (String) userMap.get("userName");
                    String merchantCode = (String) userMap.get("merchantCode");
                    Long time = (Long) userMap.get("time");
                    Long userId = (Long) userMap.get("uid");
                    Double valaidBetAmount = (Double) userMap.get("validBetAmount");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setMerchantCode(merchantCode);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(1);
                    userBonus.setTicketNum(tickets);
                    userBonus.setRemark("有效投注:" + (valaidBetAmount / 100));
                    userBonus.setLastUpdate(updateL);
                    userBonusList.add(userBonus);
                }
                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                for (Map.Entry entry : bonusMap.entrySet()) {
                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, updateL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---criterierBet差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                acBonusMapper.upsertUserBonus(reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---criterierBet插入数据---" + subBonusList.size());
                            acBonusMapper.upsertUserBonus(subBonusList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("taskId=" + taskId + "," + "criterierBet,actId=" + actId + ",task=" + taskName, e);
        }
    }


    /**
     * 每日任务
     * @param dateStr
     */
    @Override
    public void executeDailyTask(String dateStr) {
        Date now = new Date();
        long endL = now.getTime();
        long startL = now.getTime() - 1000 * 60 * 6;
        Long nowL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMdd"));
        if (StringUtils.isNotEmpty(dateStr)) {
            log.info("dateStr=" + dateStr);
            String[] dateRange = dateStr.split("-");
            String a = dateRange[0];
            String b = dateRange[1];
            long startTimeStampL = Long.parseLong(a);
            long endTimeStampL = Long.parseLong(b);
            long bias = endTimeStampL - startTimeStampL;
            int totalTimes = bias > 1000 * 60 * 5 ? (int) Math.ceil((float) bias / (1000 * 60 * 5)) : 1;
            log.info("自定义任务execute:totalTimes=" + totalTimes);
            for (int i = 0; i < totalTimes; i++) {
                startL = (long) i * 1000 * 60 * 5 + startTimeStampL;
                endL = (long) (i + 1) * 1000 * 60 * 5 + startTimeStampL;
                log.info("自定义任务:startL=" + startL + ",endL=" + endL);
                this.executeDailyTask(startL, endL, nowL);
            }
        } else {
            if (!this.queryLatestOrderExist(startL, endL)) {
                startL = now.getTime() - 1000 * 60 * 20;
                log.info("startL=" + startL + ",endL=" + endL + ",数据不存在.扩大查询范围new.startL=" + startL);
            }
            this.executeDailyTask(startL, endL, nowL);
        }
    }

    public boolean queryLatestOrderExist(long startL, long endL) {
        return activityOrderMapper.queryLatestOrderCount(startL, endL) > 0;
    }

    //每日任务： 1：每日投注x笔 2：当日单笔有效投注 >= x 元 3：当日投注注单数 >= x 笔 4：当日完成 x 笔串关玩法 5：当日完成 x 场VR体育赛事
    @Async
    public void executeDailyTask(Long startL, Long endL, Long nowL) {
        long start = System.currentTimeMillis();
        log.info("ActivityDailyJob1开始执行任务:startL=" + startL + ",endL=" + endL + ",nowL=" + nowL);
        try {
            List<Map<String, Object>> configList = acBonusMapper.queryAllTaskConfig(0);
            log.info("ActivityDailyJob共:" + (CollectionUtils.isNotEmpty(configList) ? configList.size() : 0) + "个任务");
            if (CollectionUtils.isEmpty(configList)) return;
            int total = configList.size();
            int pageSize = 4;
            int threadNum = total > pageSize ? (int) Math.ceil((float) total / pageSize) : 1;
            if (threadNum > 16) {
                threadNum = 16;
            }
            log.info("executeDailyTask,task of total=" + total + ",线程数=" + threadNum);
            CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
            try {
                List<List<Map<String, Object>>> partitionList = Lists.partition(configList, pageSize);
                startExecuteDailyTask(threadNum, threadPool, partitionList, startL, endL, nowL, countDownLatch);
                countDownLatch.await();
                log.info("executeDailyTask所有活动线程都执行处理数据完毕!" + (System.currentTimeMillis() - start));
            } catch (InterruptedException e) {
                log.error("executeDailyTask活动线程释放异常!", e);
            } finally {
                threadPool.shutdown();
            }
        } catch (Exception e) {
            log.error("ActivityDailyJob1任务执行结束ERROR!", e);
        }
        log.info("ActivityDailyJob1任务执行结束:" + (System.currentTimeMillis() - start));
    }

    private void startExecuteDailyTask(int threadNum, ExecutorService threadPool, List<List<Map<String, Object>>> partitionList, Long startL,
                                       Long endL, Long nowL, CountDownLatch countDownLatch) {
        for (int i = 0; i < threadNum; i++) {
            int finalThreadNum = i;
            List<Map<String, Object>> tempConfigList = partitionList.get(i);
            threadPool.execute(() -> {
                executeDailyTaskThread(finalThreadNum, tempConfigList, startL, endL, nowL);
                countDownLatch.countDown();
            });
        }
    }

    private void executeDailyTaskThread(int threadNum, List<Map<String, Object>> configList, Long startL, Long endL, Long nowL) {
        log.info("executeDailyTaskThread--->" + threadNum + ",任务大小:" + configList.size());
        try {
            for (Map<String, Object> config : configList) {
                String taskName = (String) config.get("taskName");
                String actName = (String) config.get("actName");
                Integer tickets = (Integer) config.get("ticketNum");
                Integer actId = (Integer) config.get("actId");
                Integer conditionId = (Integer) config.get("conditionId");
                String taskCondition = (String) config.get("taskCondition");
                String taskCondition2 = (String) config.get("taskCondition2");
                String taskCondition3 = (String) config.get("taskCondition3");
                Integer id = ((Long) config.get("id")).intValue();
                log.info("executeDailyTaskThread--->" + threadNum + "taskId:" + id + ",taskName = " + taskName + ", conditionId = " + conditionId + ", tickets = " + tickets);
                switch (conditionId) {
                    case 1:
                        dailyBetTimes(1, tickets, actName, actId, id, taskName, nowL, taskCondition, taskCondition2, taskCondition3, startL, endL);
                        break;
                    case 2:
                        dailyBetAmount(2, tickets, actName, actId, id, taskName, nowL, taskCondition, taskCondition2, taskCondition3, startL, endL);
                        break;
                    case 3:
                        dailyBetTimes(3, tickets, actName, actId, id, taskName, nowL, taskCondition, taskCondition2, taskCondition3, startL, endL);
                        break;
                    case 4:
                        dailySeriesBetTimes(4, tickets, actName, actId, id, taskName, nowL, taskCondition, taskCondition2, taskCondition3, startL, endL);
                        break;
                    case 5:
                        dailyVirtualBetTimes(5, tickets, actName, actId, id, taskName, nowL, taskCondition, taskCondition2, taskCondition3, startL, endL);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            log.error("executeDailyTaskThread--->" + threadNum, e);
        }
    }

    //每日投注x笔  {"1":[1,2,4],"2":[1,3,4],"3":[1,2,3]}
    private void dailyBetTimes(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2, String taskCondition3, Long startL, Long endL) {
        try {
            List<Integer> sportList = null, playList = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                JSONObject jsonObject = JSONObject.parseObject(taskCondition3);
                if (jsonObject != null) {
                    sportList = new ArrayList();
                    playList = new ArrayList();
                    for (String str : jsonObject.keySet()) {
                        List<Integer> playListTemp = (List<Integer>) jsonObject.get(str);
                        playList.addAll(playListTemp);
                        sportList.add(Integer.parseInt(str));
                    }
                }
            }
            long betAmount = StringUtils.isNotEmpty(taskCondition2) ? Long.parseLong(taskCondition2) : 0;
            betAmount = 100 * betAmount;
            long startTimeL = System.currentTimeMillis();
            log.info("param:taskId=" + taskId + "," + conditionId + ",dailyBetTimes:" + startL + "," + endL + "," + betAmount + "," + sportList + "," + playList);
            List<Long> userList = activityOrderMapper.queryDailyBetTimesUsers1(startL, endL, betAmount, sportList, playList);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailyBetTimes该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(userList) ? userList.size() : 0) +
                    ",cost=" + (System.currentTimeMillis() - startTimeL));
            if (CollectionUtils.isEmpty(userList)) {
                log.info(startL + "," + endL + ",dailyBetTimes没有数据");
                return;
            }
            Integer times = Integer.parseInt(taskCondition);
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime();
            Long datEndL = DateUtil.endOfDay(now).getTime();
            List<Map<String, Object>> resultList = activityOrderMapper.queryDailyBetTimesUsers2(datStartL, datEndL, betAmount, sportList, playList, times, userList);
            log.info("taskId=" + taskId + "," + datStartL + "," + datEndL + ",dailyBetTimes该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(resultList) ? resultList.size() : 0) +
                    ",cost=" + (System.currentTimeMillis() - startTimeL));
            if (CollectionUtils.isNotEmpty(resultList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : resultList) {
                    String userName = (String) userMap.get("userName");
                    String merchantCode = (String) userMap.get("merchantCode");
                    Long userId = (Long) userMap.get("uid");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setMerchantCode(merchantCode);
                    userBonus.setActId((long) actId);
                    userBonus.setActName(actName);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonusList.add(userBonus);
                }
                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                for (Map.Entry entry : bonusMap.entrySet()) {
                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailyBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                // acBonusMapper.upsertUserBonus(reduceList);
                                acBonusMapper.upsertUserBonus(reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailyBetTimes插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            acBonusMapper.upsertUserBonus(subBonusList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("dailyBetTimes,actId=" + actId + ",task=" + taskName + ",taskId=" + taskId, e);
        }
    }

    //2：当日单笔有效投注 >= x 元
    private void dailyBetAmount(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2, String taskCondition3, Long startL, Long endL) {
        try {
            BigDecimal bet = BigDecimal.valueOf(Double.parseDouble(taskCondition) * 100);
            List<Integer> sportList = null, playList = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                JSONObject jsonObject = JSONObject.parseObject(taskCondition3);
                if (jsonObject != null) {
                    sportList = new ArrayList();
                    playList = new ArrayList();
                    for (String str : jsonObject.keySet()) {
                        List<Integer> playListTemp = (List<Integer>) jsonObject.get(str);
                        playList.addAll(playListTemp);
                        sportList.add(Integer.parseInt(str));
                    }
                }
            }
            log.info("taskId=" + taskId + ",dailyBetAmount:" + startL + "," + endL + "," + bet + "," + sportList + playList);
            List<Map<String, Object>> userList = activityOrderMapper.queryBetAmountUser(bet, startL, endL, sportList, playList);
            log.info("taskId=" + taskId + ",dailyBetAmount:" + (CollectionUtils.isNotEmpty(userList) ? userList.size() : 0));
            if (CollectionUtils.isNotEmpty(userList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : userList) {
                    String userName = (String) userMap.get("userName");
                    String merchantCode = (String) userMap.get("merchantCode");
                    Long userId = (Long) userMap.get("uid");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setMerchantCode(merchantCode);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonusList.add(userBonus);
                }
                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                for (Map.Entry entry : bonusMap.entrySet()) {
//                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailyBetAmount差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                // acBonusMapper.upsertUserBonus(reduceList);
                                acBonusMapper.upsertUserBonus(reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailyBetAmount插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            acBonusMapper.upsertUserBonus(subBonusList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("dailyBetAmount,actId=" + actId + ",task=" + taskName + ",taskId=" + taskId, e);
        }
    }

    private void dailySeriesBetTimes(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2,
                                     String taskCondition3, Long startL, Long endL) {
        try {
            int times = Integer.parseInt(taskCondition);
            double betAmount = StringUtils.isNotEmpty(taskCondition2) ? Double.parseDouble(taskCondition2) : 0;
            betAmount = 100 * betAmount;
            log.info("param:taskId=" + taskId + "," + betAmount + "," + timeL + ",dailySeriesBetTimes:" + times);
            List<Long> userList = activityOrderMapper.querySeriesTimesUser1(betAmount, times, startL, endL);
            if (CollectionUtils.isEmpty(userList)) {
                log.info(startL + "," + endL + ",dailySeriesBetTimes没有数据");
                return;
            }
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime();
            Long datEndL = DateUtil.endOfDay(now).getTime();
            List<Map<String, Object>> resultList = activityOrderMapper.querySeriesTimesUser2(datStartL, datEndL, betAmount, times, userList);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailySeriesBetTimes该时间段内的有效用户有:" + (CollectionUtils.isEmpty(resultList) ? 0 : resultList.size()));
            if (CollectionUtils.isNotEmpty(resultList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : resultList) {
                    String userName = (String) userMap.get("userName");
                    String merchantCode = (String) userMap.get("merchantCode");
                    Long userId = (Long) userMap.get("uid");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setMerchantCode(merchantCode);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonusList.add(userBonus);
                }
                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                for (Map.Entry entry : bonusMap.entrySet()) {
//                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailySeriesBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                //acBonusMapper.upsertUserBonus(reduceList);
                                acBonusMapper.upsertUserBonus(reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailySeriesBetTimes插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            acBonusMapper.upsertUserBonus(subBonusList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("dailySeriesBetTimes,actId=" + actId + ",taskId=" + taskId + ",taskName=" + taskName, e);
        }
    }

    //5：当日完成 x 场VR体育赛事
    private void dailyVirtualBetTimes(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2, String taskCondition3, Long startL, Long endL) {
        try {
            List<Integer> sportList = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                sportList = new ArrayList();
                if (taskCondition3.contains(",")) {
                    String[] sportArray = taskCondition3.split(",");
                    for (String vsId : sportArray) {
                        sportList.add(Integer.parseInt(vsId.replace("\"", "")));
                    }
                } else {
                    sportList.add(Integer.parseInt(taskCondition3));
                }
            }
            long betAmount = StringUtils.isNotEmpty(taskCondition2) ? Long.parseLong(taskCondition2) : 0;
            betAmount = 100 * betAmount;
            log.info("param:taskId=" + taskId + ",dailyVirtualBetTimes:" + startL + "," + endL + "," + betAmount + "," + sportList);
            List<Long> userList = activityOrderMapper.queryDailyVirtualBetTimesUsers1(startL, endL, betAmount, sportList);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailyVirtualBetTimes该时间段内的有效用户有:" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            Integer times = Integer.parseInt(taskCondition);
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime();
            Long datEndL = DateUtil.endOfDay(now).getTime();
            List<Map<String, Object>> resultList = activityOrderMapper.queryDailyVirtualBetTimesUsers2(datStartL, datEndL, betAmount, sportList, times, userList);
            log.info("taskId=" + taskId + "," + datStartL + "," + datEndL + ",dailyVirtualBetTimes该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(resultList) ? resultList.size() : 0));
            if (CollectionUtils.isNotEmpty(resultList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : resultList) {
                    String userName = (String) userMap.get("userName");
                    String merchantCode = (String) userMap.get("merchantCode");
                    Long userId = (Long) userMap.get("uid");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setMerchantCode(merchantCode);
                    userBonus.setActId((long) actId);
                    userBonus.setActName(actName);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonusList.add(userBonus);
                }

                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                for (Map.Entry entry : bonusMap.entrySet()) {
//                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        log.info("taskId=" + taskId + "dailyVirtualBetTimes:userIdList:" + userIdList.size());
                        List<Long> receivedList = backUpAcBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailyVirtualBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                // acBonusMapper.upsertUserBonus(reduceList);
                                acBonusMapper.upsertUserBonus(reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailyVirtualBetTimes插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            acBonusMapper.upsertUserBonus(subBonusList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("dailyVirtualBetTimes,actId=" + actId + "taskId=" + taskId + "," + ",taskName=" + taskName, e);
        }
    }
}
