package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.bss.mapper.SportDutyTraderMapper;
import com.panda.sport.merchant.common.po.bss.SportDutyTraderPO;
import com.panda.sport.merchant.manage.service.SportDutyTraderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author javier
 * @date 2021/2/11
 */
@Service
@Slf4j
public class SportDutyTraderServiceImpl implements SportDutyTraderService {
    @Resource
    private SportDutyTraderMapper sportDutyTraderMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncTrader(List<SportDutyTraderPO> list) {
      //log.info("syncTrader "+list.size());
      //  //int num =sportDutyTraderMapper.deleteAll();
      //  log.info("syncTrader deleteAll"+num);
      //  //int insertNum = sportDutyTraderMapper.batchInsert(list);
      //  log.info("syncTrader insertNum"+insertNum);
    }
}
