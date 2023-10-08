package com.panda.sport.merchant.api.service.impl;

import com.panda.sport.bss.mapper.AcBonusMapper;
import com.panda.sport.merchant.api.service.ActivityService;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private AcBonusMapper acBonusMapper;


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
}
