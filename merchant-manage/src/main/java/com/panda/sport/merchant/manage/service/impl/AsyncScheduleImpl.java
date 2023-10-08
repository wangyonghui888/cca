package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.bss.mapper.AcBonusMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.util.RedisTemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/5 00:39:20
 */
@Component
@Slf4j
public class AsyncScheduleImpl {

    @Resource
    private AcBonusMapper acBonusMapper;
    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void clearGrowthTaskData() {
        long startTime = System.currentTimeMillis();
        try {
            log.info("reset growth task data start:{}", startTime);
            //acBonusMapper.resetGrowthTaskData();
            List<String> codeList = merchantMapper.getDifferentGroupMerchantList();
            log.info("clearGrowthTaskData:" + codeList);
            for (String code : codeList) {
                merchantApiClient.clearTicketsOfMardigraTask(code, 2);
                merchantApiClient.clearTicketsOfMardigraTask(code, 1);
                merchantApiClient.clearTicketsOfMardigraTask(code, 3);
            }
            acBonusMapper.deleteGrowthTaskData();
        } catch (Exception e) {
            log.error("reset growth task data error:", e);
        } finally {
            log.info("reset growth task data end, cost time:{}", System.currentTimeMillis() - startTime);
            RedisTemp.delete(Constant.IS_RESET_GROWTH_TASK_DATA);
        }
    }
}
