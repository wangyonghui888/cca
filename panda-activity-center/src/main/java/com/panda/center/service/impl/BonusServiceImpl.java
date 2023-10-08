package com.panda.center.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.panda.center.entity.AcBonusPO;
import com.panda.center.mapper.activity.AcBonusMapper;
import com.panda.center.mapper.activity.ActivityMerchantMapper;
import com.panda.center.mapper.activity.UserReport3Mapper;
import com.panda.center.mapper.trader.ActivityOrderMapper;
import com.panda.center.service.BonusService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;


@Slf4j
@Service
@RefreshScope
public class BonusServiceImpl implements BonusService {

    @Autowired
    private AcBonusMapper acBonusMapper;

    @Autowired
    private ActivityOrderMapper activityOrderMapper;

    @Autowired
    private UserReport3Mapper userReport3Mapper;


    @Autowired
    private ActivityMerchantMapper activityMerchantMapper;

    private static final Map<Integer, Object> hostMap = new HashMap<>();

    //3:主播盘 4:其他  ('赛事类型 正常-1 冠军-2 大逃杀-3 篮球-4 主播盘-5 足球-6')
    @PostConstruct
    private void init() {
        List<Integer> hostList = Arrays.asList(5);
        hostMap.put(4, hostList);
        List<Integer> otherList = Arrays.asList(1, 2, 3, 4, 6);
        hostMap.put(3, otherList);
    }

    @Override
    @Async
    public void clearDailyTask() {
        try {
            long start = System.currentTimeMillis();
            log.info("ActivityClearDataJob1clear任务:" + start);
            acBonusMapper.clearTicketsOfTask();
            log.info("清空每日任务数据成功!");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.setFirstDayOfWeek(Calendar.MONDAY);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            log.info("dayOfWeek=" + dayOfWeek + ",dayOfMonth=" + dayOfMonth);
            if (dayOfWeek == 2) {
                acBonusMapper.clearTicketsOfMardigraTask(2);
                log.info("清理每周玩家成长任务数据完成!");
            }
            if (dayOfMonth == 1) {
                acBonusMapper.clearTicketsOfMardigraTask(1);
                log.info("1清理每月玩家成长任务数据完成!");
                acBonusMapper.clearTicketsOfMardigraTask(3);
                log.info("3清理每月玩家成长任务数据完成!");
            }
            log.info("ActivityClearDataJob1clear任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("ActivityClearDataJob1任务执行结束ERROR!", e);
        }
    }

    //每日任务： 1：每日投注x笔 2：当日单笔有效投注 >= x 元 3：当日投注注单数 >= x 笔 4：当日完成 x 笔串关玩法 5：当日完成 x 场VR体育赛事
    //3:主播盘 4:其他  ('赛事类型 正常-1 冠军-2 大逃杀-3 篮球-4 主播盘-5 足球-6')
    @Override
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
                log.info("ActivityDailyJob1任务执行结束:" + (System.currentTimeMillis() - start));
            }
        } catch (Exception e) {
            log.error("ActivityDailyJob1任务执行结束ERROR!", e);
        }
    }

    private void startExecuteDailyTask(int threadNum, ExecutorService threadPool, List<List<Map<String, Object>>> partitionList,
                                       Long startL, Long endL, Long nowL, CountDownLatch countDownLatch) {
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

    //成长任务： 1：本月累计投注 x 元 2：本周累计有效投注 >= x 元 3：本月累计有效投注 >= x 元
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
                log.info("ActivitySummerJob2任务执行成长结束:" + (System.currentTimeMillis() - start));
            }
        } catch (Exception e) {
            log.error("ActivitySummerJob2任务执行结束ERROR!", e);
        }
    }

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
                log.info("executeSumTaskThread--->" + threadNum + ",taskName=" + taskName + ",id=" + id + ",tickets=" + tickets + ",acStartDayL=" + acStartDayL);
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
        Long acStartDayL = 0l;
        try {
            Map<String, Object> summerTask = activityMerchantMapper.getSummerTaskActivity();
            log.info("getActivityStartTime,summerTask=" + summerTask);
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

    //1：本月累计投注day
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
            List<Map<String, Object>> userList = (List<Map<String, Object>>) this.queryOlympicBetEvery(BigDecimal.valueOf(betAmount), startMonthDateL, endMonthDateL, days);
            log.info("taskId=" + taskId + "," + conditionId + ",betEvery.queryOlympicBetEvery 获取数据" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isNotEmpty(userList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : userList) {
                    String userName = (String) userMap.get("userName");
                    Long time = (Long) userMap.get("days");
                    Long userId = (Long) userMap.get("uid");
                    BigDecimal valaidBetAmount = (BigDecimal) userMap.get("validBetAmount");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(taskType);
                    userBonus.setTicketNum(tickets);
                    userBonus.setRemark("days=" + time + ",有效投注:" + (valaidBetAmount));
                    userBonus.setLastUpdate(monthL);
                    userBonusList.add(userBonus);
                }
                List<Long> userIdList = userBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    List<Long> receivedList = acBonusMapper.queryAllReceivedUserListByTime(taskId, monthL, userIdList);
                    if (CollectionUtils.isNotEmpty(receivedList)) {
                        List<AcBonusPO> reduceList = userBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                        log.info("taskId=" + taskId + "---betEvery差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            acBonusMapper.upsertUserBonus(reduceList);
                    } else {
                        log.info("taskId=" + taskId + "---betEvery插入数据---" + userBonusList.size());
                        acBonusMapper.upsertUserBonus(userBonusList);
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
            List<Map<String, Object>> userList = (List<Map<String, Object>>) this.queryOlympicMardiGras(BigDecimal.valueOf(betAmount), startDateL, endDateL);
            log.info("taskId=" + taskId + "," + conditionId + ",criterierBet=" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isNotEmpty(userList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : userList) {
                    String userName = (String) userMap.get("userName");
                    Long time = (Long) userMap.get("time");
                    Long userId = (Long) userMap.get("uid");
                    BigDecimal validBet = (BigDecimal) userMap.get("validBetAmount");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(1);
                    userBonus.setTicketNum(tickets);
                    userBonus.setRemark("有效投注:" + (validBet));
                    userBonus.setLastUpdate(updateL);
                    userBonusList.add(userBonus);
                }
                List<Long> userIdList = userBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    List<Long> receivedList = acBonusMapper.queryAllReceivedUserListByTime(taskId, updateL, userIdList);
                    if (CollectionUtils.isNotEmpty(receivedList)) {
                        List<AcBonusPO> reduceList = userBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                        log.info("taskId=" + taskId + "---criterierBet差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            acBonusMapper.upsertUserBonus(reduceList);
                    } else {
                        log.info("taskId=" + taskId + "---criterierBet插入数据---" + userBonusList.size());
                        acBonusMapper.upsertUserBonus(userBonusList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("taskId=" + taskId + "," + "criterierBet,actId=" + actId + ",task=" + taskName, e);
        }
    }

    //每日投注x笔  {"1":[1,2,4],"2":[1,3,4],"3":[1,2,3]}
    private void dailyBetTimes(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2, String taskCondition3, Long startL, Long endL) {
        try {
            List<Long> sportList = null;
            List<Integer> playList = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                JSONObject jsonObject = JSONObject.parseObject(taskCondition3);
                if (jsonObject != null) {
                    sportList = new ArrayList();
                    playList = new ArrayList();
                    log.info("dailyBetTimes:" + jsonObject);
                    for (String str : jsonObject.keySet()) {
                        List<Integer> playListTemp = (List<Integer>) jsonObject.get(str);
                        playList.addAll(playListTemp);
                        sportList.add(Long.parseLong(str));
                    }
                }
            }
            long betAmount = StringUtils.isNotEmpty(taskCondition2) ? Long.parseLong(taskCondition2) : 0;
            long startTimeL = System.currentTimeMillis();
            playList = assemblyHostMarketData(playList);
            log.info("param:taskId=" + taskId + "," + conditionId + ",dailyBetTimes:" + startL + "," + endL + "," + betAmount + ",sportList=" + sportList + "," + playList);
            List<Long> userList = activityOrderMapper.queryDJDailyBetTimesUsers1(startL, endL, betAmount, sportList, playList);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailyBetTimes该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(userList) ? userList.size() : 0) +
                    ",cost=" + (System.currentTimeMillis() - startTimeL));
            if (CollectionUtils.isEmpty(userList)) {
                log.info(startL + "," + endL + ",dailyBetTimes没有数据");
                return;
            }
            Integer times = Integer.parseInt(taskCondition);
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime() / 1000;
            Long datEndL = DateUtil.endOfDay(now).getTime() / 1000;
            List<Map<String, Object>> resultList = activityOrderMapper.queryDJDailyBetTimesUsers2(datStartL, datEndL, betAmount, sportList, playList, times, userList);
            log.info("taskId=" + taskId + "," + datStartL + "," + datEndL + ",dailyBetTimes该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(resultList) ? resultList.size() : 0) +
                    ",cost=" + (System.currentTimeMillis() - startTimeL));
            if (CollectionUtils.isNotEmpty(resultList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : resultList) {
                    String userName = (String) userMap.get("userName");
                    Long userId = ((BigInteger) userMap.get("uid")).longValue();
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setActId((long) actId);
                    userBonus.setActName(actName);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setRemark(conditionId + "");
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonusList.add(userBonus);
                }
                List<Long> userIdList = userBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    List<Long> receivedList = acBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                    if (CollectionUtils.isNotEmpty(receivedList)) {
                        List<AcBonusPO> reduceList = userBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                        log.info("taskId=" + taskId + "---dailyBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            acBonusMapper.upsertUserBonus(reduceList);
                    } else {
                        log.info("taskId=" + taskId + "---dailyBetTimes插入数据---" + userBonusList.size());
                        acBonusMapper.upsertUserBonus(userBonusList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("dailyBetTimes,actId=" + actId + ",task=" + taskName + ",taskId=" + taskId, e);
        }
    }

    //3:主播盘 4:其他  ('赛事类型 正常-1 冠军-2 大逃杀-3 篮球-4 主播盘-5 足球-6')
    private List<Integer> assemblyHostMarketData(List<Integer> playList) {
        if (CollectionUtils.isNotEmpty(playList)) {
            Set<Integer> resultList = new HashSet<>();
            for (Integer id : playList) {
                log.info("assemblyHostMarketData:id:" + id);
                List<Integer> tempList = (List<Integer>) hostMap.get(id);
                log.info("assemblyHostMarketData:id:" + id + ",tempList=" + tempList);
                resultList.addAll(tempList);
            }
            return new ArrayList<>(resultList);
        }
        return playList;
    }

    //5：当日完成 x 场VR体育赛事
    private void dailyVirtualBetTimes(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2, String taskCondition3, Long startL, Long endL) {
        try {
            List<Long> sportList = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                sportList = new ArrayList();
                if (taskCondition3.contains(",")) {
                    String[] sportArray = taskCondition3.split(",");
                    for (String vsId : sportArray) {
                        sportList.add(Long.parseLong(vsId.replace("\"", "")));
                    }
                } else {
                    sportList.add(Long.parseLong(taskCondition3));
                }
            }
            long betAmount = StringUtils.isNotEmpty(taskCondition2) ? Long.parseLong(taskCondition2) : 0;
            log.info("param:taskId=" + taskId + ",dailyVirtualBetTimes:" + startL + "," + endL + "," + betAmount + "," + sportList);
            List<Long> userList = activityOrderMapper.queryDJDailyVirtualBetTimesUsers1(startL, endL, betAmount, sportList);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailyVirtualBetTimes该时间段内的有效用户有:" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            Integer times = Integer.parseInt(taskCondition);
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime() / 1000;
            Long datEndL = DateUtil.endOfDay(now).getTime() / 1000;
            List<Map<String, Object>> resultList = activityOrderMapper.queryDJDailyVirtualBetTimesUsers2(datStartL, datEndL, betAmount, sportList, times, userList);
            log.info("taskId=" + taskId + "," + datStartL + "," + datEndL + ",dailyVirtualBetTimes该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(resultList) ? resultList.size() : 0));
            if (CollectionUtils.isNotEmpty(resultList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : resultList) {
                    String userName = (String) userMap.get("userName");
                    Long userId = (Long) userMap.get("uid");
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setActId((long) actId);
                    userBonus.setActName(actName);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonus.setRemark(conditionId + "");
                    userBonusList.add(userBonus);
                }
                List<Long> userIdList = userBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    List<Long> logList;
                    if (userIdList.size() > 10) {
                        logList = userIdList.subList(0, 9);
                    } else {
                        logList = userIdList;
                    }
                    log.info("taskId=" + taskId + "dailyVirtualBetTimes:userIdList:" + logList);
                    List<Long> receivedList = acBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                    if (CollectionUtils.isNotEmpty(receivedList)) {
                        List<AcBonusPO> reduceList = userBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                        log.info("taskId=" + taskId + "---dailyVirtualBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            acBonusMapper.upsertUserBonus(reduceList);
                    } else {
                        log.info("taskId=" + taskId + "---dailyVirtualBetTimes插入数据---" + userBonusList.size());
                        acBonusMapper.upsertUserBonus(userBonusList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("dailyVirtualBetTimes,actId=" + actId + "taskId=" + taskId + "," + ",taskName=" + taskName, e);
        }
    }

    //2：当日单笔有效投注 >= x 元
    private void dailyBetAmount(Integer conditionId, Integer tickets, String actName, Integer actId, Integer taskId, String taskName, Long timeL, String taskCondition, String taskCondition2, String taskCondition3, Long startL, Long endL) {
        try {
            BigDecimal bet = BigDecimal.valueOf(Double.parseDouble(taskCondition));
            List<Long> sportList = null;
            List<Integer> playList = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                JSONObject jsonObject = JSONObject.parseObject(taskCondition3);
                if (jsonObject != null) {
                    sportList = new ArrayList();
                    playList = new ArrayList();
                    log.info("dailyBetAmount:" + jsonObject);
                    for (String str : jsonObject.keySet()) {
                        List<Integer> playListTemp = (List<Integer>) jsonObject.get(str);
                        playList.addAll(playListTemp);
                        sportList.add(Long.parseLong(str));
                    }
                }
            }
            playList = assemblyHostMarketData(playList);
            log.info("taskId=" + taskId + ",dailyBetAmount:" + startL + "," + endL + ",bet=" + bet + ",sportList=" + sportList + playList);
            List<Map<String, Object>> userList = activityOrderMapper.queryDJBetAmountUser(bet, startL, endL, sportList, playList);
            log.info("taskId=" + taskId + ",dailyBetAmount:" + (CollectionUtils.isNotEmpty(userList) ? userList.size() : 0));
            if (CollectionUtils.isNotEmpty(userList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : userList) {
                    String userName = (String) userMap.get("userName");
                    Long userId = ((BigInteger) userMap.get("uid")).longValue();
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonus.setRemark(conditionId + "");
                    userBonusList.add(userBonus);
                }
                List<Long> userIdList = userBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    List<Long> receivedList = acBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                    if (CollectionUtils.isNotEmpty(receivedList)) {
                        List<AcBonusPO> reduceList = userBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                        log.info("taskId=" + taskId + "---dailyBetAmount差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            acBonusMapper.upsertUserBonus(reduceList);
                    } else {
                        log.info("taskId=" + taskId + "---dailyBetAmount插入数据---" + userBonusList.size());
                        acBonusMapper.upsertUserBonus(userBonusList);
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
            Integer orderType = null;
            if (StringUtils.isNotEmpty(taskCondition3)) {
                orderType = Integer.parseInt(taskCondition3);
            }
            double betAmount = StringUtils.isNotEmpty(taskCondition2) ? Double.parseDouble(taskCondition2) : 0;
            log.info("param:taskId=" + taskId + "," + betAmount + "," + timeL + ",dailySeriesBetTimes:" + times + ",orderType=" + orderType);
            List<Long> userList = activityOrderMapper.queryDJSeriesTimesUser1(betAmount, times, startL, endL, orderType);
            if (CollectionUtils.isEmpty(userList)) {
                log.info(startL + "," + endL + ",dailySeriesBetTimes没有数据");
                return;
            }
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime() / 1000;
            Long datEndL = DateUtil.endOfDay(now).getTime() / 1000;
            List<Map<String, Object>> resultList = activityOrderMapper.queryDJSeriesTimesUser2(datStartL, datEndL, betAmount, times, userList, orderType);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailySeriesBetTimes该时间段内的有效用户有:" + (CollectionUtils.isEmpty(resultList) ? 0 : resultList.size()));
            if (CollectionUtils.isNotEmpty(resultList)) {
                List<AcBonusPO> userBonusList = new ArrayList<>();
                for (Map<String, Object> userMap : resultList) {
                    String userName = (String) userMap.get("userName");
                    Long userId = ((BigInteger) userMap.get("uid")).longValue();
                    AcBonusPO userBonus = new AcBonusPO();
                    userBonus.setUid(userId);
                    userBonus.setUserName(userName);
                    userBonus.setActName(actName);
                    userBonus.setActId((long) actId);
                    userBonus.setTaskId((long) taskId);
                    userBonus.setTaskName(taskName);
                    userBonus.setBonusType(3);
                    userBonus.setTaskType(0);
                    userBonus.setTicketNum(tickets);
                    userBonus.setLastUpdate(timeL);
                    userBonus.setRemark(conditionId + "");
                    userBonusList.add(userBonus);
                }
                List<Long> userIdList = userBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    List<Long> receivedList = acBonusMapper.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                    if (CollectionUtils.isNotEmpty(receivedList)) {
                        List<AcBonusPO> reduceList = userBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                        log.info("taskId=" + taskId + "---dailySeriesBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            acBonusMapper.upsertUserBonus(reduceList);
                    } else {
                        log.info("taskId=" + taskId + "---dailySeriesBetTimes插入数据---" + userBonusList.size());
                        acBonusMapper.upsertUserBonus(userBonusList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("dailySeriesBetTimes,actId=" + actId + ",taskId=" + taskId + ",taskName=" + taskName, e);
        }
    }

    public List<?> queryOlympicMardiGras(BigDecimal betAmount, Long startDateL, Long endDateL) {
        try {
            Date now = new Date();
            long endHourTimeL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMddHH"));
            Date lastExecuteTime = DateUtil.offsetHour(now, -1);
            Long startHourTimeL = Long.parseLong(DateFormatUtils.format(lastExecuteTime, "yyyyMMddHH"));
            log.info("queryOlympicMardiGras:" + startHourTimeL + "," + endHourTimeL + ",startDateL=" + startDateL + ",endDateL=" + endDateL + ",betAmount=" + betAmount);
            long start = System.currentTimeMillis();
            List<Map<String, Object>> resultList = userReport3Mapper.queryOlympicMardiGras(betAmount, startDateL, endDateL, startHourTimeL);
            log.info("queryOlympicMardiGras:" + (CollectionUtils.isNotEmpty(resultList) ? resultList.size() : resultList) +
                    ",cost:" + (System.currentTimeMillis() - start));
            return resultList;
        } catch (Exception e) {
            log.error("ActivityServiceImpl.queryOlympicMardiGras,exception:", e);
            return null;
        }
    }

    public List<?> queryOlympicBetEvery(BigDecimal betAmount, Long startDateL, Long endDateL, Integer days) {
        try {
            Date now = new Date();
            long endHourTimeL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMddHH"));
            Date lastExecuteTime = DateUtil.offsetHour(now, -1);
            Long startHourTimeL = Long.parseLong(DateFormatUtils.format(lastExecuteTime, "yyyyMMddHH"));
            log.info("queryOlympicBetEvery:" + startHourTimeL + "," + endHourTimeL);
            long start = System.currentTimeMillis();
            List<Map<String, Object>> resultList =
                    userReport3Mapper.queryOlympicBetEvery(betAmount, startDateL, endDateL, startHourTimeL, days);
            log.info("queryOlympicBetEvery:" + (CollectionUtils.isNotEmpty(resultList) ? resultList.size() : resultList) +
                    ",cost:" + (System.currentTimeMillis() - start));
            return resultList;
        } catch (Exception e) {
            log.error("ActivityServiceImpl.queryOlympicBetEvery,exception:", e);
            return null;
        }
    }

}
