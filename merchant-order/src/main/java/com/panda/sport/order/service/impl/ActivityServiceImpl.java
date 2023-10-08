package com.panda.sport.order.service.impl;


import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.panda.sport.backup.mapper.ActivityOrderMapper;
import com.panda.sport.backup.mapper.BackUpAcBonusMapper;
import com.panda.sport.bss.mapper.AcBonusMapper;
import com.panda.sport.bss.mapper.ActivityMerchantMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.order.feign.MerchantApiClient;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.ActivityService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service("activityService")
@RefreshScope
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private AcBonusMapper acBonusMapper;
    @Autowired
    private BackUpAcBonusMapper backUpAcBonusMapper;

    @Autowired
    private ActivityOrderMapper activityOrderMapper;

    @Autowired
    private MerchantReportClient reportClient;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private ActivityMapperService activityMapperService;

    @Autowired
    private ActivityMerchantMapper activityMerchantMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Value("${activity.start:20210924}")
    private String acStartDay;

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Override
    public Response getActivityBetStatList(ActivityBetStatDTO vo) {
        return Response.returnSuccess(reportClient.getActivityBetStatList(vo));
    }

    @Override
    public void exportExcel(ActivityBetStatDTO vo, HttpServletResponse response, HttpServletRequest request) {
        List<?> resultList = reportClient.exportExcel(vo);
        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }
        try {
            log.info("运营管理-活动投注统计的导出");
            String fileName = "merchantActivityStatReport.csv";
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            FileCopyUtils.copy(exportActivtyDayCsv(resultList), response.getOutputStream());

            /**
             * 写入日志
             */
            MerchantLogPO logPO = new MerchantLogPO();
            Map<String, String> userInfo = ExtractUserInfoUtil.extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(userId);
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(119);
            logPO.setTypeName("excel报表导出");
            logPO.setPageName("运营管理-活动投注统计");
            logPO.setPageCode("");
            if (vo.getMerchantCodeList() != null && vo.getMerchantCodeList().size() > 0) {
                logPO.setMerchantCode(vo.getMerchantCodeList().toString());
            }
            logPO.setMerchantName("excel报表导出");
            logPO.setOperatField(JsonUtils.listToJson(Collections.singletonList("excel报表导出")));
            logPO.setDataId(JsonUtils.listToJson(Collections.singletonList(vo)));

            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            logPO.setIp(IPUtils.getIpAddr(request));
            merchantLogService.saveLog(logPO);


        } catch (Exception e) {
            log.error("导出活动报表异常!" + e.getMessage(), e);
        }

    }

    @Override
    public Map<String, Object> exportExcelV2(ActivityBetStatDTO vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = vo.getLanguage();
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        try {
            merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "活动投注统计导出_" : "Report Center-activityCountExport"
                    , null, vo.getOperUsername(), JSON.toJSONString(vo),
                    language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "运营活动-活动投注统计下载" : "Report Center-activityCountExport", "activityCountExportServiceImpl", null);
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    @Override
    public boolean queryLatestOrderExist(long startL, long endL) {
        return activityOrderMapper.queryLatestOrderCount(startL, endL) > 0;
    }

    @Override
    public void executeMatchUserMidTask(long startL, long endL) {
        List<MatchUserMidVO> mList = activityOrderMapper.queryMatchUserMidInfoList(startL, endL);
        if (CollectionUtils.isNotEmpty(mList)) {
            activityOrderMapper.saveMatchUserMidInfoList(mList);
        }
    }


    /**
     * 导出用户到csv文件
     * String.valueOf不能删除
     *
     * @param mapList 导出的数据
     */
    private byte[] exportActivtyDayCsv(List<?> mapList) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> filterList = mapper.convertValue(mapList, new TypeReference<List<Map<String, String>>>() {
        });
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        for (Map<String, String> map : filterList) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", map.get("uid").concat("\t"));
            rowData.put("2", map.get("username"));
            rowData.put("3", map.get("merchantCode").concat("\t"));
            rowData.put("4", map.get("validBetNums"));
            rowData.put("5", map.get("validBetAmount"));
            rowData.put("6", map.get("validBetDays"));
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getHeader(), exportData);
    }

    /**
     * 获取excel头
     *
     * @return
     */
    private LinkedHashMap<String, String> getHeader() {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "用户ID");
        header.put("2", "用户名");
        header.put("3", "所属商户编码");
        header.put("4", "投注笔数");
        header.put("5", "累计投注额度");
        header.put("6", "累计投注有效天数");
        return header;
    }

    @Override
    @Async
    public void clearDailyTask() {
        try {
            long start = System.currentTimeMillis();
            log.info("ActivityClearDataJob1clear任务:" + start);
            List<String> codeList = merchantMapper.getDifferentGroupMerchantList();
            log.info("clearDailyTask:" + codeList);
            for (String code : codeList) {
                merchantApiClient.clearTicketsOfTask(code);
            }
            //acBonusMapper.clearTicketsOfTask();
            log.info("清空每日任务数据成功!");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.setFirstDayOfWeek(Calendar.MONDAY);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            log.info("dayOfWeek=" + dayOfWeek + ",dayOfMonth=" + dayOfMonth);
            if (dayOfWeek == 2) {
                for (String code : codeList) {
                    merchantApiClient.clearTicketsOfMardigraTask(code, 2);
                }
                // acBonusMapper.clearTicketsOfMardigraTask(2);
                log.info("清理每周玩家成长任务数据完成!");
            }
            if (dayOfMonth == 1) {
                for (String code : codeList) {
                    merchantApiClient.clearTicketsOfMardigraTask(code, 1);
                }
                // acBonusMapper.clearTicketsOfMardigraTask(1);
                log.info("1清理每月玩家成长任务数据完成!");
                for (String code : codeList) {
                    merchantApiClient.clearTicketsOfMardigraTask(code, 3);
                }
                //acBonusMapper.clearTicketsOfMardigraTask(3);
                log.info("3清理每月玩家成长任务数据完成!");
            }
            log.info("ActivityClearDataJob1clear任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("ActivityClearDataJob1任务执行结束ERROR!", e);
        }
    }

    //每日任务： 1：每日投注x笔 2：当日单笔有效投注 >= x 元 3：当日投注注单数 >= x 笔 4：当日完成 x 笔串关玩法 5：当日完成 x 场VR体育赛事
    //6 ： 注册场馆天数   7 ： 首次投注场馆天数   8  ： 总投注金额 9  ： 总输赢金额
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
                if (StringUtils.isNotEmpty(taskCondition) && !JosnToUtil.isJSON2(taskCondition)) { //原来逻辑
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
                } else {//新2383逻辑
                    Long createTime = (Long) config.get("createTime");
                    Map<String, Object> map = JosnToUtil.get(taskCondition, nowL, startL, endL);
                    Integer conditionMatch = (Integer) config.get("conditionMatch");
                    dailyAcTaskBonus(map, tickets, actName, actId, id, taskName, nowL, startL, endL, conditionMatch, createTime);
                }
            }
        } catch (Exception e) {
            log.error("executeDailyTaskThread--->" + threadNum, e);
        }
    }

    /**
     * 每日任务：
     * 1：每日投注x笔
     * 2：当日单笔有效投注 >= x 元
     * 3：当日投注注单数 >= x 笔
     * 4：当日完成 x 笔串关玩法
     * 5：当日完成 x 场VR体育赛事
     * 6:注册场馆天数：依据每日（自然日）为计算基准，注册时间點（3/21 23:59：第一天)到隔日（3/22 00:01：第二天)，抓取对应用户群。（可配置，条件为『天数』）
     * 7:首次投注场馆天数：依据第一张注单投注时间（时间点同注册逻辑），抓取对应用户群（可配置，条件为『天数』）
     * 8:总投注金额：依据日期条件，计算所有投注金额总计，抓取对应用户群（可配置，条件为『投注金额』，『日期区间』）
     * 9:总输赢金额：依据日期条件，计算所有投注投注输赢总计，抓取对应用户群，（可配置，条件为『输赢金额』，『日期区间』）
     * 10:任务条件最低投注
     * 1，2，3，5，6，10条件类型list
     */
    private void dailyAcTaskBonus(Map<String, Object> maps, Integer tickets, String actName, Integer actId, Integer taskId,
                                  String taskName, Long timeL, Long startL, Long endL, Integer conditionMatch, Long updateTime) {
        List<AcTaskParamVO> paramVOs = null;
        List<Long> userList = new ArrayList<>();
        List<String> listIds = null;
        if (null != maps && maps.size() > 0) {
            paramVOs = (List<AcTaskParamVO>) maps.get("acTaskParamList");
            listIds = (List<String>) maps.get("conditionIdList");
        }
        try {
            long startTimeL = System.currentTimeMillis();
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime();
            Long datEndL = DateUtil.endOfDay(now).getTime();
            /**
             * 數據优先级处理
             */
            List<AcTaskParamVO> params = JosnToUtil.getSortInfo(paramVOs, listIds);
            log.info("dailyAcTaskBonus.params:" + params);
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<Long> tempResult = new ArrayList<>();
            for (AcTaskParamVO paramVO : params) {
                List<Map<String, Object>> thisResultList = null;
                switch (paramVO.getConditionId()) {
                    case 1:
                        thisResultList = getMonAllInfoOne(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    case 2:
                        thisResultList = getMonAllInfoTwo(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    case 3:
                        thisResultList = getMonAllInfoTree(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    case 4:
                        thisResultList = getMonAllInfoFour(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    case 5:
                        thisResultList = getMonAllInfoFive(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    case 6:
                        thisResultList = getMonAllInfoSix(taskId, userList, paramVO, startL, endL, datStartL, datEndL, updateTime,timeL);
                        break;
                    case 7:
                        thisResultList = getMonAllInfoSeven(taskId, userList, paramVO, startL, endL, datStartL, datEndL ,timeL);
                        break;
                    case 8:
                    case 9:
                        thisResultList = dailyUserEnd(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    case 10:
                        thisResultList = getMonAllInfoTen(userList, paramVO, startL, endL, datStartL, datEndL);
                        break;
                    default:
                        break;
                }
                log.info("dailyAcTaskBonus.taskId=" + taskId + ",case=" + paramVO.getConditionId() + ",datStartL=" + datStartL + ",datEndL=" + datEndL + ",startL=" +startL + ",endL"+endL + ",thisResultList=" +
                        (CollectionUtils.isNotEmpty(thisResultList) ? thisResultList.size() : 0) + ",cost=" + (System.currentTimeMillis() - startTimeL));
                if (null == conditionMatch || 0 == conditionMatch) {
                    if (CollectionUtils.isEmpty(thisResultList)) return;
                    userList = thisResultList.stream().map(e -> (Long) e.get("uid")).collect(Collectors.toList());
                    resultList = thisResultList;
                } else {
                    if (CollectionUtils.isNotEmpty(thisResultList)) {
                        resultList.addAll(thisResultList);
                    }
                }
            }
            if (CollectionUtils.isEmpty(resultList)) {
                return;
            }
            resultList = resultList.stream().filter(e -> {
                boolean bool = !tempResult.contains((Long) e.get("uid"));
                tempResult.add((Long) e.get("uid"));
                return bool;
            }).collect(toList());
            log.info("saveBonusInfoList准备保持奖卷数据taskId=" + taskId + ",数据=" + tempResult.size());
            saveBonusInfoList(tickets, actName, (long) actId, taskId, taskName, timeL, resultList);
        } catch (Exception e) {
            log.error("dailyAcTaskBonus,actId=" + actId + ",task=" + taskName + ",taskId=" + taskId, e);
        }
    }


    //1：每日投注x笔
    private List<Map<String, Object>> getMonAllInfoOne(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        long startTimeL = System.currentTimeMillis();
        List<Map<String, Object>> thisResultList = getUserMaps(userList, paramVO, startL, endL, datStartL, datEndL);
        log.info("getMonAllInfoOne.activityOrderMapper=" + startL + "," + endL + ",daily该时间段内的有效用户有:" + (CollectionUtils.isNotEmpty(thisResultList) ? thisResultList.size() : 0) +
                ",cost=" + (System.currentTimeMillis() - startTimeL) + paramVO);
        if (CollectionUtils.isEmpty(thisResultList)) return null;
        return thisResultList;
    }

    // * 2：当日单笔有效投注 >= x 元
    private List<Map<String, Object>> getMonAllInfoTwo(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        Set<Long> userSt = activityOrderMapper.getDailyAcTaskBonusUser210(startL, endL, userList, paramVO);
        if (CollectionUtils.isEmpty(userSt)) {
            return null;
        }
        List<Map<String, Object>> thisResultList = dailyUserOne(userSt, paramVO, datStartL, datEndL);
        if (CollectionUtils.isEmpty(thisResultList)) return null;
        return thisResultList;
    }

    //     * 3：当日投注注单数 >= x 笔
    private List<Map<String, Object>> getMonAllInfoTree(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        List<Map<String, Object>> thisResultList = getUserMaps(userList, paramVO, startL, endL, datStartL, datEndL);
        if (CollectionUtils.isEmpty(thisResultList)) return null;
        return thisResultList;
    }

    //4：当日完成 x 笔串关玩法
    private List<Map<String, Object>> getMonAllInfoFour(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        List<Map<String, Object>> thisResultList;
        Set<Long> userSt = activityOrderMapper.getDaily4AcTaskBonusUserList(startL, endL, userList, paramVO);
        if (CollectionUtils.isEmpty(userSt)) {
            return null;
        }
        thisResultList = dailyUserFour(userSt, paramVO, datStartL, datEndL);
        return thisResultList;
    }

    // 5：当日完成 x 场VR体育赛事
    private List<Map<String, Object>> getMonAllInfoFive(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        List<Map<String, Object>> thisResultList = getUserMaps(userList, paramVO, startL, endL, datStartL, datEndL);
        if (CollectionUtils.isEmpty(thisResultList)) return null;
        return thisResultList;
    }

    //6:注册场馆天数：依据每日（自然日）为计算基准，注册时间點（3/21 23:59：第一天)到隔日（3/22 00:01：第二天)，抓取对应用户群。（可配置，条件为『天数』）
    private List<Map<String, Object>> getMonAllInfoSix(Integer taskId, List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL, Long createTime,Long timeL) {

        /**
         * 处理是否有已经有生产奖卷的uid
         */
        List<Long> haveUserList = null;
        if (CollectionUtils.isEmpty(userList)) {
            userList=null;
            haveUserList = getHaveTaskBonusUser(taskId, timeL);
        }
        /**
         * 如果任务创建时间小于注册时间条件可以做增量处理
         */
        long minData;
        if (null == paramVO.getBeforeTime()) {
            minData = paramVO.getAfterTime();
        } else if (null == paramVO.getAfterTime()) {
            minData = paramVO.getBeforeTime();
        } else {
            minData = Math.min(paramVO.getBeforeTime(), paramVO.getAfterTime());
        }
        /**
         * 查询符合条件的用户id
         */
        long startTimeL = System.currentTimeMillis();
        List<Map<String, Object>> resultList;
        if (minData < createTime) {
            resultList = activityOrderMapper.queryRegisterTimesUserSix(userList, paramVO, haveUserList);
        } else {
            List<Long> users = activityOrderMapper.getDaily6AcTaskBonusUser(startL, endL, userList, haveUserList);
            if (CollectionUtils.isEmpty(users)) return null;
            resultList = activityOrderMapper.queryRegisterTimesUserSix(users, paramVO, null);
        }
        log.info("getMonAllInfoOne.daily6SeriesBetSix条件=" + paramVO.getConditionId() + ",今天已生成奖卷用户数="+ (CollectionUtils.isEmpty(haveUserList) ? 0 : haveUserList.size())+",最终结果数=" + (CollectionUtils.isEmpty(resultList) ? 0 : resultList.size()) + ",cost=" + (System.currentTimeMillis() - startTimeL));
        return resultList;
    }


    private List<Long> getHaveTaskBonusUser(Integer taskId, Long timeL) {
        List<Long> haveUserList = activityOrderMapper.getHaveTaskBonusUser(taskId, timeL);
        return haveUserList;
    }

    //7:首次投注场馆天数：依据第一张注单投注时间（时间点同注册逻辑），抓取对应用户群（可配置，条件为『天数』）
    private List<Map<String, Object>> getMonAllInfoSeven(Integer taskId, List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL,Long timeL) {
        /**
         * 处理是否有已经有生产奖卷的uid
         */
        List<Long> haveUserList = null;
        if (CollectionUtils.isEmpty(userList)) {
            userList=null;
            haveUserList = getHaveTaskBonusUser(taskId,timeL);
        }
        paramVO.setUserList(userList);
        paramVO.setHaveUserList(haveUserList);
        List<Map<String, Object>> result = reportClient.queryAcSevenTaskUserInfo(startL, endL, datStartL, datEndL, paramVO);
        return result;
    }

    private List<Map<String, Object>> getMonAllInfoTen(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        Set<Long> userSt = activityOrderMapper.getDailyAcTaskBonusUser210(startL, endL, userList, paramVO);
        if (CollectionUtils.isEmpty(userSt)) {
            return null;
        }
        List<Map<String, Object>> thisResultList = dailyUserOne(userSt, paramVO, datStartL, datEndL);
        if (CollectionUtils.isEmpty(thisResultList)) return null;
        return thisResultList;
    }

    private List<Map<String, Object>> getUserMaps(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        Set<Long> userSt = activityOrderMapper.getDailyAcTaskBonusUser(startL, endL, userList, paramVO);
        if (CollectionUtils.isEmpty(userSt)) {
            return null;
        }
        List<Map<String, Object>> thisResultList = dailyUserOne(userSt, paramVO, datStartL, datEndL);
        return thisResultList;
    }

    /**
     * 8:总投注金额：依据日期条件，计算所有投注金额总计，抓取对应用户群（可配置，条件为『投注金额』，『日期区间』）
     * 9:总输赢金额：依据日期条件，计算所有投注投注输赢总计，抓取对应用户群，（可配置，条件为『输赢金额』，『日期区间』）
     */
    private List<Map<String, Object>> dailyUserEnd(List<Long> userList, AcTaskParamVO paramVO, Long startL, Long endL, Long datStartL, Long datEndL) {
        Date now = new Date();
        long endHourTimeL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMddHH"));
        Date lastExecuteTime = DateUtil.offsetHour(now, -1);
        Long startHourTimeL = Long.parseLong(DateFormatUtils.format(lastExecuteTime, "yyyyMMddHH"));
        log.info("queryOlympicBetEvery:" + startHourTimeL + "," + endHourTimeL);
        log.info("每日任务queryAcTaskUserInfo:" + paramVO);
        if (8 == paramVO.getConditionId() || 9 == paramVO.getConditionId()) {
            Date startDate = DateUtils.strToDate(paramVO.getStartTime()),
                    endDate = DateUtils.strToDate(paramVO.getEndTime());
            long startDateLong = startDate.getTime();
            long endDateLong = endDate.getTime();
            if (!((startDateLong <= datStartL) && (datStartL <= endDateLong))) {
                return null;
            }
        }
        paramVO.setUserList(userList);
        List<Map<String, Object>> result = reportClient.queryAcTaskUserInfo(startL, endL, datStartL, startHourTimeL, paramVO);
        return result;
    }


    private List<Map<String, Object>> dailyUserFour(Set<Long> userList, AcTaskParamVO acTaskParamVO, Long datStartL, Long datEndL) {
        long startTimeL = System.currentTimeMillis();
        List<Map<String, Object>> result = activityOrderMapper.query4SeriesTimesUser(datStartL, datEndL, userList, acTaskParamVO);
        log.info("getMonAllInfoOne.dailyUserFour数据,4" + (CollectionUtils.isEmpty(result) ? 0 : result.size()) + ",时间开始=" + datStartL + ",时间结束=" + datEndL + ",cost=" + (System.currentTimeMillis() - startTimeL));
        return result;
    }

    private List<Map<String, Object>> dailyUserOne(Set<Long> userList, AcTaskParamVO acTaskParamVO, Long datStartL, Long datEndL) {
        long startTimeL = System.currentTimeMillis();
        List<Map<String, Object>> result = activityOrderMapper.getDayDailyAcTaskBonusUser(datStartL, datEndL, userList, acTaskParamVO);
        log.info("getMonAllInfoOne.getDayDailyAcTaskBonusUser条件=" + acTaskParamVO.getConditionId() + ",开始时间=" + datStartL + ",结束时间=" + datEndL + ",结果数=" + (CollectionUtils.isEmpty(result) ? 0 : result.size()) + ",cost=" + (System.currentTimeMillis() - startTimeL));
        return result;
    }


    private void saveBonusInfoList(Integer tickets, String actName, long actId, Integer taskId, String taskName, Long timeL, List<Map<String, Object>> resultList) {
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
                userBonus.setActId(actId);
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
                        log.info("timeL" + timeL + ",taskId=" + taskId + "---saveBonusInfoList差集 reduce2(list2-list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                        if (CollectionUtils.isNotEmpty(reduceList))
                            merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                    } else {
                        log.info("timeL" + timeL + ",taskId=" + taskId + "---saveBonusInfoList插入数据---" + subBonusList.size());
                        merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
                    }
                }
            }
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
            }
            log.info("ActivitySummerJob2任务执行成长结束:" + (System.currentTimeMillis() - start));
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
                                merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                        } else {
                            log.info(merchantCode + "taskId=" + taskId + "---betEvery插入数据---" + subBonusList.size() + subBonusList);
                            merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
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
                                //acBonusMapper.upsertUserBonus(reduceList);
                                merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---criterierBet插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
                        }
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
            //List<Long> userList = activityOrderMapper.queryDailyBetTimesUsers1(startL, endL, betAmount, sportList, playList);
            List<Long> userList = activityMapperService.queryDailyBetTimesUsers1(startL, endL, betAmount, sportList, playList);
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
            //List<Map<String, Object>> resultList = activityOrderMapper.queryDailyBetTimesUsers2(datStartL, datEndL, betAmount, sportList, playList, times, userList);
            List<Map<String, Object>> resultList = activityMapperService.queryDailyBetTimesUsers2(datStartL, datEndL, betAmount, sportList, playList, times, userList);
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
                        List<Long> receivedList = activityMapperService.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailyBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                // acBonusMapper.upsertUserBonus(reduceList);
                                merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailyBetTimes插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("dailyBetTimes,actId=" + actId + ",task=" + taskName + ",taskId=" + taskId, e);
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
            List<Long> userList = activityMapperService.queryDailyVirtualBetTimesUsers1(startL, endL, betAmount, sportList);
            log.info("taskId=" + taskId + "," + startL + "," + endL + ",dailyVirtualBetTimes该时间段内的有效用户有:" + (CollectionUtils.isEmpty(userList) ? 0 : userList.size()));
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            Integer times = Integer.parseInt(taskCondition);
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime();
            Long datEndL = DateUtil.endOfDay(now).getTime();
            List<Map<String, Object>> resultList = activityMapperService.queryDailyVirtualBetTimesUsers2(datStartL, datEndL, betAmount, sportList, times, userList);
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
                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        log.info("taskId=" + taskId + "dailyVirtualBetTimes:userIdList:" + userIdList.size());
                        List<Long> receivedList = activityMapperService.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailyVirtualBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                // acBonusMapper.upsertUserBonus(reduceList);
                                merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailyVirtualBetTimes插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
                        }
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
            List<Map<String, Object>> userList = activityMapperService.queryBetAmountUser(bet, startL, endL, sportList, playList);
            log.info("taskId=" + taskId + ",userList:" + (CollectionUtils.isNotEmpty(userList) ? userList.size() : 0));
            log.info("ActivityTask-userList" + userList);
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
                log.info("ActivityTask-userBonusList" + userBonusList);
                Map<String, List<AcBonusPO>> bonusMap = userBonusList.stream().filter(e -> e.getMerchantCode() != null).collect(Collectors.groupingBy(AcBonusPO::getMerchantCode));
                log.info("ActivityTask-bonusMap=" + bonusMap);
                for (Map.Entry entry : bonusMap.entrySet()) {
                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    log.info("ActivityTask-userIdList=" + userIdList);
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = activityMapperService.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        log.info("receivedList=" + receivedList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---reduceList reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                // acBonusMapper.upsertUserBonus(reduceList);
                                merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailyBetAmount插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
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
            List<Long> userList = activityMapperService.querySeriesTimesUser1(betAmount, times, startL, endL);
            if (CollectionUtils.isEmpty(userList)) {
                log.info(startL + "," + endL + ",dailySeriesBetTimes没有数据");
                return;
            }
            Date now = new Date();
            Long datStartL = DateUtil.beginOfDay(now).getTime();
            Long datEndL = DateUtil.endOfDay(now).getTime();
            List<Map<String, Object>> resultList = activityMapperService.querySeriesTimesUser2(datStartL, datEndL, betAmount, times, userList);
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
                    String merchantCode = (String) entry.getKey();
                    List<AcBonusPO> subBonusList = (List<AcBonusPO>) entry.getValue();
                    List<Long> userIdList = subBonusList.stream().map(AcBonusPO::getUid).collect(toList());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        List<Long> receivedList = activityMapperService.queryAllReceivedUserListByTime(taskId, timeL, userIdList);
                        if (CollectionUtils.isNotEmpty(receivedList)) {
                            List<AcBonusPO> reduceList = subBonusList.stream().filter(item -> !receivedList.contains(item.getUid())).collect(toList());
                            log.info("taskId=" + taskId + "---dailySeriesBetTimes差集 reduce2 (list2 - list1)---" + (CollectionUtils.isEmpty(reduceList) ? 0 : reduceList.size()));
                            if (CollectionUtils.isNotEmpty(reduceList))
                                //acBonusMapper.upsertUserBonus(reduceList);
                                merchantApiClient.upsertUserBonus(merchantCode, reduceList);
                        } else {
                            log.info("taskId=" + taskId + "---dailySeriesBetTimes插入数据---" + subBonusList.size());
                            //acBonusMapper.upsertUserBonus(subBonusList);
                            merchantApiClient.upsertUserBonus(merchantCode, subBonusList);
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("dailySeriesBetTimes,actId=" + actId + ",taskId=" + taskId + ",taskName=" + taskName, e);
        }
    }
}
