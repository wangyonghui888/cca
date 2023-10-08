package com.panda.sport.order.service.impl;


import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.feign.PandaBSSUsercenterClient;
import com.panda.sport.order.service.UserAccountClearDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("userAccountClearDataService")
@RefreshScope
public class UserAccountClearDataServiceImpl implements UserAccountClearDataService {
    /**
     * 单次批量数量
     */
    @Value("${user.clear.batch_count:10000}")
    private Integer BATCH_COUNT;

    @Value("${user.clear.merchantCodeStr:oubao,719919,105627,472028,553452,2N12,2I859,2G325,2M175}")
    private String merchantCodeStr;

    @Autowired
    private MerchantReportClient merchantReportClient;

    @Autowired
    private PandaBSSUsercenterClient usercenterClient;

    @Autowired
    private BackupTUserMapper tUserMapper;

    @Override
    @Async
    public void userAccountClearDataTask(Integer num) {
        try {
            long start = System.currentTimeMillis();
            log.info(num + ":UserAccountClearDataJob任务:" + start);
            List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
            log.info("userAccountClearDataTask:" + merchantCodeList);
            if (num == null) {
                num = BATCH_COUNT;
            }

            if (num <= BATCH_COUNT) {
                for (String str : merchantCodeList) {
                    Long userId = tUserMapper.getOneUid(str);
                    if (userId != null) {
                        usercenterClient.userAccountClearDataTask(str, BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userAccountClearDataTask1:" + BATCH_COUNT);
                    }
                }
            } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                        Long userId = tUserMapper.getOneUid(str);
                        if (userId != null) {
                            usercenterClient.userAccountClearDataTask(str, BATCH_COUNT);
                            Thread.sleep(10000);
                            log.info(str + ":userAccountClearDataTask2:" + BATCH_COUNT);
                        }
                    }
                }
            }
            log.info("UserAccountClearDataJob.userAccountClearDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("UserAccountClearDataJob任务执行结束ERROR!", e);
        }
    }

    @Override
    @Async
    public void userAccountAllClearDataTask(Integer num) {
        try {
            long start = System.currentTimeMillis();
            log.info(num + ":UserAccountClearDataJob任务:" + start);
            List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
            log.info("userAccountClearDataTask:" + merchantCodeList);
            if (num == null) {
                num = BATCH_COUNT;
            }

            if (num <= BATCH_COUNT) {
                for (String str : merchantCodeList) {
                        merchantReportClient.userOrderAllClearDataTask(str, BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userAccountClearDataTask1:" + BATCH_COUNT);
                }
            } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                            merchantReportClient.userOrderAllClearDataTask(str, BATCH_COUNT);
                            Thread.sleep(10000);
                            log.info(str + ":userAccountClearDataTask2:" + BATCH_COUNT);
                    }
                }
            }
            log.info("UserAccountClearDataJob.userAccountClearDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("UserAccountClearDataJob任务执行结束ERROR!", e);
        }
    }

    @Override
    @Async
    public void acUserOrderDateClearDataTask(Long nowL) {
        merchantReportClient.acUserOrderDateClearDataTask(nowL);
    }

    @Override
    @Async
    public void userOrderDayClearDataTask(Integer num) {
        try {
            long start = System.currentTimeMillis();
            List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
            log.info("userAccountClearDataTask:" + merchantCodeList);
            if (num == null) {
                num = BATCH_COUNT;
            }

            if (num <= BATCH_COUNT) {
                for (String str : merchantCodeList) {
                    merchantReportClient.userOrderDayClearDataTask(str, BATCH_COUNT);
                    Thread.sleep(10000);
                    log.info(str + ":userOrderDayClearDataTask1:" + BATCH_COUNT);
                }
            } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                        merchantReportClient.userOrderDayClearDataTask(str, BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userOrderDayClearDataTask2:" + BATCH_COUNT);
                    }
                }
            }
            log.info("UserAccountClearDataJob.userOrderDayClearDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("userOrderDayClearDataTaskJob任务执行结束ERROR!", e);
        }
    }

    @Override
    @Async
    public void userOrderDayUtfClearDataTask(Integer num) {
        try {
            long start = System.currentTimeMillis();
            log.info(num + ":userOrderDayUtfClearDataTask任务:" + start);
            List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
            log.info("userOrderDayUtfClearDataTask:" + merchantCodeList);
            if (num == null) {
                num = BATCH_COUNT;
            }

            if (num <= BATCH_COUNT) {
                for (String str : merchantCodeList) {
                    merchantReportClient.userOrderDayUtfClearDataTask(str, BATCH_COUNT);
                    Thread.sleep(10000);
                    log.info(str + ":userOrderDayUtfClearDataTask1:" + BATCH_COUNT);
                }
            } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                        merchantReportClient.userOrderDayUtfClearDataTask(str, BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userOrderDayUtfClearDataTask2:" + BATCH_COUNT);
                    }
                }
            }
            log.info("userOrderDayUtfClearDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("userOrderDayUtfClearDataTaskJob任务执行结束ERROR!", e);
        }
    }

    @Override
    @Async
    public void userOrderMonthClearDataTask(Integer num) {
        try {
            long start = System.currentTimeMillis();
            log.info(num + ":userOrderMonthClearDataTask任务:" + start);
            List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
            log.info("userOrderMonthClearDataTask:" + merchantCodeList);
            if (num == null) {
                num = BATCH_COUNT;
            }

            if (num <= BATCH_COUNT) {
                for (String str : merchantCodeList) {
                    merchantReportClient.userOrderMonthClearDataTask(str, BATCH_COUNT);
                    Thread.sleep(10000);
                    log.info(str + ":userOrderMonthClearDataTask1:" + BATCH_COUNT);
                }
            } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                        merchantReportClient.userOrderMonthClearDataTask(str, BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userOrderMonthClearDataTask2:" + BATCH_COUNT);
                    }
                }
            }
            log.info("userOrderMonthClearDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("userOrderMonthClearDataTaskJob任务执行结束ERROR!", e);
        }
    }

    @Override
    @Async
    public void userOrderMonthUtfClearDataTask(Integer num) {
        try {
            long start = System.currentTimeMillis();
            log.info(num + ":userOrderMonthUtfClearDataTask任务:" + start);
            List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
            log.info("userOrderMonthUtfClearDataTask:" + merchantCodeList);
            if (num == null) {
                num = BATCH_COUNT;
            }

            if (num <= BATCH_COUNT) {
                for (String str : merchantCodeList) {
                    merchantReportClient.userOrderMonthUtfClearDataTask(str, BATCH_COUNT);
                    Thread.sleep(10000);
                    log.info(str + ":userOrderMonthUtfClearDataTask1:" + BATCH_COUNT);
                }
            } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                        merchantReportClient.userOrderMonthUtfClearDataTask(str, BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userOrderMonthUtfClearDataTask2:" + BATCH_COUNT);
                    }
                }
            }
            log.info("userOrderMonthUtfClearDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("userOrderMonthUtfClearDataTask任务执行结束ERROR!", e);
        }
    }

    @Override
    @Async
    public void userSummaryLibraryDataTask(Integer num) {
     try {
        long start = System.currentTimeMillis();
        log.info(num + ":userSummaryLibraryDataTask:" + start);
        List<String> merchantCodeList = Arrays.asList(merchantCodeStr.split(","));
        log.info("userSummaryLibraryDataTask:" + merchantCodeList);
        if (num == null) {
            num = BATCH_COUNT;
        }
        if (num <= BATCH_COUNT) {
            for (String str : merchantCodeList) {
                extractedDeleteUser(str,BATCH_COUNT);
                Thread.sleep(10000);
                log.info(str + ":userSummaryLibraryDataTask1:" + BATCH_COUNT);
            }
        } else {
                int times = num / BATCH_COUNT;
                //分批次清理数据
                for (int i = 0; i < times; i++) {
                    for (String str : merchantCodeList) {
                        extractedDeleteUser(str,BATCH_COUNT);
                        Thread.sleep(10000);
                        log.info(str + ":userSummaryLibraryDataTask2:" + BATCH_COUNT);
                    }
                }
            }
            log.info("userSummaryLibraryDataTask任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("userSummaryLibraryDataTask任务执行结束ERROR!", e);
        }
    }

    private void extractedDeleteUser(String merchantCode,Integer num) {
        try {
            //用户总数量除以设置总条数得到循环次数
            int count = num / 1000;
            if (num % 1000 > 0) {
                count = count + 1;
            }
            long time = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                log.info(count + ":extractedDeleteUser:第" + i + "次执行!");
                List<Long> userIds = tUserMapper.getDeleteUidList(merchantCode);
                if (CollectionUtils.isNotEmpty(userIds)) {
                    //删除用户数据
                    tUserMapper.clearUserDailyTask(userIds);
                    log.info(count + ":extractedDeleteUser:第" + i + "次执行成功!!! 共cost:" + (System.currentTimeMillis() - time));
                }
            }
        } catch (Exception e) {
            log.error(merchantCode + ":extractedDeleteUser" + num, e);
        }
    }
}
