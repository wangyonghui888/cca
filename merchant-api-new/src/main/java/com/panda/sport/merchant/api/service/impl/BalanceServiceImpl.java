package com.panda.sport.merchant.api.service.impl;


import com.panda.sport.bss.mapper.TAccountMapper;
import com.panda.sport.merchant.api.config.DataSourceConstant;
import com.panda.sport.merchant.api.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RefreshScope
@Service("balanceService")
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private TAccountMapper accountMapper;

    @Override
    @Transactional(transactionManager = DataSourceConstant.MERCHANT_MASTER_TRANSACTION_MANAGER)
    public BigDecimal getUserBalance(Long userId) {
        BigDecimal origin = accountMapper.getUserBalance(userId).multiply(new BigDecimal(100));
        return origin;
    }
}
