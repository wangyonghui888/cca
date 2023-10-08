package com.panda.sport.merchant.api.service;

import java.math.BigDecimal;

public interface BalanceService {

    BigDecimal getUserBalance(Long userId);
}
