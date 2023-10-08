package com.panda.center.service.impl;

import com.panda.center.constant.Constant;
import com.panda.center.mapper.activity.AcBonusMapper;
import com.panda.center.utils.RedisTemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Async("asyncServiceExecutor")
    @Transactional(rollbackFor = Exception.class)
    public void clearGrowthTaskData() {
        long startTime = System.currentTimeMillis();
        try {
            log.info("reset growth task data start:{}", startTime);
            //acBonusMapper.resetGrowthTaskData();
            acBonusMapper.deleteGrowthTaskData();
        } catch (Exception e) {
            log.error("reset growth task data error:", e);
        } finally {
            log.info("reset growth task data end, cost time:{}", System.currentTimeMillis() - startTime);
            RedisTemp.delete(Constant.DJ_MANAGE_GROWTH_TASK_RESET);
        }
    }

}
