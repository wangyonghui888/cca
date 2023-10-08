package com.panda.sport.merchant.api.service;

import com.panda.sport.merchant.common.po.bss.AcBonusPO;

import java.util.List;

public interface ActivityService {


    void updateUserBonus(List<AcBonusPO> bonusList);

    void clearTicketsOfTask(String merchantCode);

    void clearTicketsOfMardigraTask(String merchantCode, Integer conditionId);
}
